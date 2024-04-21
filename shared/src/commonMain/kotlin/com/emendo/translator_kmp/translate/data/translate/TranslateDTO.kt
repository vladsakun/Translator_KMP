package com.emendo.translator_kmp.translate.data.translate

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class TranslateDTO(
  @SerialName("q") val textToTranslate: String,
  @SerialName("source") val sourceLanguageCode: String,
  @SerialName("target") val targetLanguageCode: String,
)