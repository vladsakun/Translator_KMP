package com.emendo.translator_kmp.voice_to_text.domain

import com.emendo.translator_kmp.core.domain.util.CommonStateFlow

interface VoiceToTextParser {
  val state: CommonStateFlow<VoiceToTextParserState>
  fun startListening(languageCode: String)
  fun stopListening()
  fun cancel()
  fun reset()
}