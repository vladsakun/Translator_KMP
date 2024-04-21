package com.emendo.translator_kmp.android.translate.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emendo.translator_kmp.translate.domain.history.HistoryDataSource
import com.emendo.translator_kmp.translate.domain.translate.TranslateUseCase
import com.emendo.translator_kmp.translate.presentation.TranslateEvent
import com.emendo.translator_kmp.translate.presentation.TranslateViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AndroidTranslateViewModel @Inject constructor(
  private val translateUseCase: TranslateUseCase,
  private val historyDataSource: HistoryDataSource,
) : ViewModel() {

  private val viewModel by lazy {
    TranslateViewModel(
      translateUseCase = translateUseCase,
      historyDataSource = historyDataSource,
      coroutineScope = viewModelScope,
    )
  }

  val state = viewModel.state
  fun onEvent(event: TranslateEvent) = viewModel.onEvent(event)
}