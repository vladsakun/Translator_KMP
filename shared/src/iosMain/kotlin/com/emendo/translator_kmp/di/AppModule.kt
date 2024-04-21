package com.emendo.translator_kmp.di

import com.emendo.translator_kmp.database.TranslatorDatabase
import com.emendo.translator_kmp.translate.data.history.SqlDelightHistoryDataSource
import com.emendo.translator_kmp.translate.data.local.DatabaseDriverFactory
import com.emendo.translator_kmp.translate.data.remote.HttpClientFactory
import com.emendo.translator_kmp.translate.data.translate.KtorTranslateClient
import com.emendo.translator_kmp.translate.domain.history.HistoryDataSource
import com.emendo.translator_kmp.translate.domain.translate.TranslateClient
import com.emendo.translator_kmp.translate.domain.translate.TranslateUseCase

class AppModule {

  val historyDataSource: HistoryDataSource by lazy {
    SqlDelightHistoryDataSource(
      TranslatorDatabase(
        DatabaseDriverFactory().create()
      )
    )
  }

  private val translateClient: TranslateClient by lazy {
    KtorTranslateClient(HttpClientFactory().create())
  }

  val translateUseCase: TranslateUseCase by lazy {
    TranslateUseCase(
      client = translateClient,
      historyDataSource = historyDataSource,
    )
  }
}