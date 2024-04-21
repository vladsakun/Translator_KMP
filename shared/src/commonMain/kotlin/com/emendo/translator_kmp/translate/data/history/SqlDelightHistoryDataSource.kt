package com.emendo.translator_kmp.translate.data.history

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.emendo.translator_kmp.core.domain.util.CommonFlow
import com.emendo.translator_kmp.core.domain.util.toCommonFlow
import com.emendo.translator_kmp.database.TranslatorDatabase
import com.emendo.translator_kmp.translate.domain.history.HistoryDataSource
import com.emendo.translator_kmp.translate.domain.history.HistoryItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock

class SqlDelightHistoryDataSource(
  db: TranslatorDatabase,
) : HistoryDataSource {

  private val queries = db.translateQueries

  override fun getHistory(): CommonFlow<List<HistoryItem>> {
    return queries
      .getHistory()
      .asFlow()
      .mapToList(Dispatchers.IO)
      .map { history ->
        history.map { it.toHistoryItem() }
      }
      .toCommonFlow()
  }

  override suspend fun insertHistoryItem(historyItem: HistoryItem) {
    queries.insertHistory(
      id = historyItem.id,
      fromLanguageCode = historyItem.fromLanguageCode,
      fromText = historyItem.fromText,
      toLanguageCode = historyItem.toLanguageCode,
      toText = historyItem.toText,
      timestamp = Clock.System.now().toEpochMilliseconds(),
    )
  }
}