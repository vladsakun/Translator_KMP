package com.emendo.translator_kmp.voice_to_text.domain

data class VoiceToTextParserState(
  val result: String = "",
  val error: String? = null,
  val powerRation: Float = 0f,
  val isSpeaking: Boolean = false,
)