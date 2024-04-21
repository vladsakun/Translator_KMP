package com.emendo.translator_kmp.translate.presentation

import com.emendo.translator_kmp.core.domain.util.Resource
import com.emendo.translator_kmp.core.domain.util.toCommonStateFlow
import com.emendo.translator_kmp.core.presentation.UiLanguage
import com.emendo.translator_kmp.translate.domain.history.HistoryDataSource
import com.emendo.translator_kmp.translate.domain.translate.TranslateException
import com.emendo.translator_kmp.translate.domain.translate.TranslateUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class TranslateViewModel(
  private val translateUseCase: TranslateUseCase,
  private val historyDataSource: HistoryDataSource,
  private val coroutineScope: CoroutineScope?,
) {

  private val viewModelScope = coroutineScope ?: CoroutineScope(Dispatchers.Main)

  private val _state = MutableStateFlow(TranslateState())
  val state = combine(historyDataSource.getHistory(), _state) { history, state ->
    if (state.history != history) {
      state.copy(history = history.mapNotNull {
        UiHistoryItem(
          id = it.id ?: return@mapNotNull null,
          fromText = it.fromText,
          toText = it.toText,
          fromLanguage = UiLanguage.byCode(it.fromLanguageCode),
          toLanguage = UiLanguage.byCode(it.toLanguageCode),
        )
      })
    } else {
      state
    }
  }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000L), TranslateState())
    .toCommonStateFlow()

  private var translateJob: Job? = null

  fun onEvent(event: TranslateEvent) {
    when (event) {
      is TranslateEvent.ChangeTranslationText -> {
        _state.update {
          it.copy(fromText = event.text)
        }
      }

      is TranslateEvent.ChooseFromLanguage -> {
        _state.update {
          it.copy(
            isChoosingFromLanguage = false,
            fromLanguage = event.language,
          )
        }
      }

      is TranslateEvent.ChooseToLanguage -> {
        val newState = _state.updateAndGet {
          it.copy(
            isChoosingToLanguage = false,
            toLanguage = event.language,
          )
        }
        translate(newState)
      }

      TranslateEvent.CloseTranslation -> {
        _state.update {
          it.copy(
            isTranslating = false,
            fromText = "",
            toText = null,
          )
        }
      }

      TranslateEvent.EditTranslation -> {
        if (state.value.toText != null) {
          _state.update {
            it.copy(
              toText = null,
              isTranslating = false,
            )
          }
        }
      }

      TranslateEvent.OnErrorSeen -> {
        _state.update { it.copy(error = null) }
      }

      TranslateEvent.OpenFromLanguageDropDown -> {
        _state.update { it.copy(isChoosingFromLanguage = true) }
      }

      TranslateEvent.OpenToLanguageDropDown -> {
        _state.update { it.copy(isChoosingToLanguage = true) }
      }

      is TranslateEvent.SelectHistoryItem -> {
        translateJob?.cancel()
        _state.update {
          it.copy(
            fromText = event.item.fromText,
            toText = event.item.toText,
            fromLanguage = event.item.fromLanguage,
            toLanguage = event.item.toLanguage,
            isTranslating = false,
          )
        }
      }

      TranslateEvent.StopChoosingLanguage -> {
        _state.update {
          it.copy(
            isChoosingFromLanguage = false,
            isChoosingToLanguage = false,
          )
        }
      }

      is TranslateEvent.SubmitVoiceResult -> {
        _state.update {
          it.copy(
            fromText = event.result ?: it.fromText,
            isTranslating = if (event.result != null) false else it.isTranslating,
            toText = if (event.result != null) null else it.toText,
          )
        }
      }

      TranslateEvent.SwapLanguages -> {
        _state.update {
          it.copy(
            fromLanguage = it.toLanguage,
            toLanguage = it.fromLanguage,
            fromText = it.toText ?: "",
            toText = if (it.toText != null) it.fromText else null,
          )
        }
      }

      TranslateEvent.Translate -> translate(state.value)
      else -> Unit
    }
  }

  private fun translate(state: TranslateState) {
    if (state.isTranslating || state.fromText.isBlank()) {
      return
    }

    translateJob = viewModelScope.launch {
      _state.update {
        it.copy(isTranslating = true)
      }
      val result = translateUseCase(
        fromLanguageCode = state.fromLanguage.language,
        fromText = state.fromText,
        toLanguageCode = state.toLanguage.language,
      )
      when (result) {
        is Resource.Success -> {
          _state.update {
            it.copy(
              toText = result.data,
              isTranslating = false,
              error = null,
            )
          }
        }

        is Resource.Error -> {
          _state.update {
            it.copy(
              isTranslating = false,
              error = (result.throwable as? TranslateException)?.error,
            )
          }
        }
      }
    }
  }
}