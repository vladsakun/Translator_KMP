package com.emendo.translator_kmp.translate.data.translate

import kotlinx.serialization.Serializable

@Serializable
data class TranslatedDTO(
  val translatedText: String,
)