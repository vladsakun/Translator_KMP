package com.emendo.translator_kmp.android.translate.presentation

import android.speech.tts.TextToSpeech
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.unit.dp
import com.emendo.translator_kmp.android.translate.presentation.components.LanguageDropDown
import com.emendo.translator_kmp.android.translate.presentation.components.SwapLanguagesButton
import com.emendo.translator_kmp.android.translate.presentation.components.TranslateTextField
import com.emendo.translator_kmp.translate.presentation.TranslateEvent
import com.emendo.translator_kmp.translate.presentation.TranslateState
import com.emendo.translator_kmp.android.R
import com.emendo.translator_kmp.android.translate.presentation.components.rememberTextToSpeech
import java.util.Locale

@Composable
fun TranslateScreen(
  state: TranslateState,
  onEvent: (TranslateEvent) -> Unit,
) {
  val context = LocalContext.current
  Scaffold(
    floatingActionButton = {

    }
  ) { paddingValues ->
    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues)
        .padding(16.dp),
      verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
      item {
        Row(
          modifier = Modifier.fillMaxWidth(),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween,
        ) {
          LanguageDropDown(
            language = state.fromLanguage,
            isOpen = state.isChoosingFromLanguage,
            onClick = {
              onEvent(TranslateEvent.OpenFromLanguageDropDown)
            },
            onDismiss = {
              onEvent(TranslateEvent.StopChoosingLanguage)
            },
            onSelectLanguage = {
              onEvent(TranslateEvent.ChooseFromLanguage(it))
            }
          )
          Spacer(modifier = Modifier.weight(1f))
          SwapLanguagesButton(onClick = { onEvent(TranslateEvent.SwapLanguages) })
          Spacer(modifier = Modifier.weight(1f))
          LanguageDropDown(
            language = state.toLanguage,
            isOpen = state.isChoosingToLanguage,
            onClick = {
              onEvent(TranslateEvent.OpenToLanguageDropDown)
            },
            onDismiss = {
              onEvent(TranslateEvent.StopChoosingLanguage)
            },
            onSelectLanguage = {
              onEvent(TranslateEvent.ChooseToLanguage(it))
            }
          )
        }
      }
      item {
        val clipboardManager = LocalClipboardManager.current
        val keyboardController = LocalSoftwareKeyboardController.current
        val tts = rememberTextToSpeech()
        TranslateTextField(
          fromText = state.fromText,
          toText = state.toText,
          isTranslating = state.isTranslating,
          fromLanguage = state.fromLanguage,
          toLanguage = state.toLanguage,
          onTranslateClick = {
            keyboardController?.hide()
            onEvent(TranslateEvent.Translate)
          },
          onTextChange = { onEvent(TranslateEvent.ChangeTranslationText(it)) },
          onCopyClick = {
            clipboardManager.setText(annotatedString = buildAnnotatedString { append(it) })
            Toast.makeText(context, context.getString(R.string.copied_to_clipboard), Toast.LENGTH_SHORT).show()
          },
          onCloseClick = { onEvent(TranslateEvent.CloseTranslation) },
          onSpeakerClick = {
            tts.language = state.toLanguage.toLocale() ?: Locale.ENGLISH
            tts.speak(state.toText, TextToSpeech.QUEUE_FLUSH, null, null)
          },
          onTextFieldClick = { onEvent(TranslateEvent.EditTranslation) },
          modifier = Modifier.fillMaxWidth()
        )
      }
    }
  }
}