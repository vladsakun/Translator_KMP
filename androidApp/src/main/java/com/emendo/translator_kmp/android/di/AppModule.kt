package com.emendo.translator_kmp.android.di

import android.app.Application
import app.cash.sqldelight.db.SqlDriver
import com.emendo.translator_kmp.database.TranslatorDatabase
import com.emendo.translator_kmp.translate.data.history.SqlDelightHistoryDataSource
import com.emendo.translator_kmp.translate.data.local.DatabaseDriverFactory
import com.emendo.translator_kmp.translate.data.remote.HttpClientFactory
import com.emendo.translator_kmp.translate.data.translate.KtorTranslateClient
import com.emendo.translator_kmp.translate.domain.history.HistoryDataSource
import com.emendo.translator_kmp.translate.domain.translate.TranslateClient
import com.emendo.translator_kmp.translate.domain.translate.TranslateUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

  @Provides
  @Singleton
  fun provideHttpClient(): HttpClient {
    return HttpClientFactory().create()
  }

  @Provides
  @Singleton
  fun provideTranslateClient(httpClient: HttpClient): TranslateClient {
    return KtorTranslateClient(httpClient)
  }

  @Provides
  @Singleton
  fun provideDatabaseDriverFactory(app: Application): SqlDriver {
    return DatabaseDriverFactory(app).create()
  }

  @Provides
  @Singleton
  fun provideHistoryDataSource(driver: SqlDriver): HistoryDataSource {
    return SqlDelightHistoryDataSource(TranslatorDatabase(driver))
  }

  @Provides
  @Singleton
  fun provideTranslationUseCase(
    client: TranslateClient,
    dataSource: HistoryDataSource,
  ): TranslateUseCase = TranslateUseCase(client, dataSource)
}