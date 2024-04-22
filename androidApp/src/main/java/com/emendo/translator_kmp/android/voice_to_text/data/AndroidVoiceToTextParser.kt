package com.emendo.translator_kmp.android.voice_to_text.data

import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.SpeechRecognizer.ERROR_CLIENT
import com.emendo.translator_kmp.android.R
import com.emendo.translator_kmp.core.domain.util.CommonStateFlow
import com.emendo.translator_kmp.core.domain.util.toCommonStateFlow
import com.emendo.translator_kmp.voice_to_text.domain.VoiceToTextParser
import com.emendo.translator_kmp.voice_to_text.domain.VoiceToTextParserState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class AndroidVoiceToTextParser(
  private val app: Application,
) : VoiceToTextParser, RecognitionListener {

  private val recognizer = SpeechRecognizer.createSpeechRecognizer(app)

  private val _state = MutableStateFlow(VoiceToTextParserState())
  override val state: CommonStateFlow<VoiceToTextParserState> = _state.toCommonStateFlow()

  override fun startListening(languageCode: String) {
    _state.update { VoiceToTextParserState() }

    if (!SpeechRecognizer.isRecognitionAvailable(app)) {
      _state.update { it.copy(error = app.getString(R.string.error_speech_recognition_unavailable)) }
      return
    }

    val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
      putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
      putExtra(RecognizerIntent.EXTRA_LANGUAGE, languageCode)
    }

    recognizer.setRecognitionListener(this)
    recognizer.startListening(intent)
    _state.update { it.copy(isSpeaking = true) }
  }

  override fun stopListening() {
    _state.update { VoiceToTextParserState() }
    recognizer.stopListening()
  }

  override fun cancel() {
    recognizer.cancel()
  }

  override fun reset() {
    _state.update { VoiceToTextParserState() }
  }

  override fun onReadyForSpeech(params: Bundle?) {
    _state.update { it.copy(error = null) }
  }

  override fun onBeginningOfSpeech() = Unit

  override fun onRmsChanged(rmsdB: Float) {
    _state.update {
      it.copy(
        powerRation = rmsdB * (1f / 12f - (-2f))
      )
    }
  }

  override fun onBufferReceived(buffer: ByteArray?) = Unit

  override fun onEndOfSpeech() {
    _state.update { it.copy(isSpeaking = false) }
  }

  override fun onError(error: Int) {
    if (error == ERROR_CLIENT) {
      return
    }
    _state.update { it.copy(error = "Error: $error") }
  }

  override fun onResults(results: Bundle?) {
    results
      ?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
      ?.firstOrNull()
      ?.let { text ->
        _state.update {
          it.copy(result = text)
        }
      }
  }

  override fun onPartialResults(partialResults: Bundle?) = Unit

  override fun onEvent(eventType: Int, params: Bundle?) = Unit
}