package com.emendo.translator_kmp.translate.presentation

import com.emendo.translator_kmp.core.presentation.UiLanguage

data class UiHistoryItem(
  val id: Long,
  val fromText: String,
  val toText: String,
  val fromLanguage: UiLanguage,
  val toLanguage: UiLanguage,
)