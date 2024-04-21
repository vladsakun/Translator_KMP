package com.emendo.translator_kmp.translate.domain.history

import com.emendo.translator_kmp.core.domain.util.CommonFlow

interface HistoryDataSource {
  fun getHistory(): CommonFlow<List<HistoryItem>>
  suspend fun insertHistoryItem(historyItem: HistoryItem)
}