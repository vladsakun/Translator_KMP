package com.emendo.translator_kmp.translate.data.history

import com.emendo.translator_kmp.translate.domain.history.HistoryItem
import database.HistroyEntity

fun HistroyEntity.toHistoryItem(): HistoryItem {
  return HistoryItem(
    id = id,
    fromLanguageCode = fromLanguageCode,
    fromText = fromText,
    toLanguageCode = toLanguageCode,
    toText = toText
  )
}