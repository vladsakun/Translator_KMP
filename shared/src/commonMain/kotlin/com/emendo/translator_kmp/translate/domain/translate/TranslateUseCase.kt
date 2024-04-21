package com.emendo.translator_kmp.translate.domain.translate

import com.emendo.translator_kmp.core.domain.language.Language
import com.emendo.translator_kmp.core.domain.util.Resource
import com.emendo.translator_kmp.translate.domain.history.HistoryDataSource
import com.emendo.translator_kmp.translate.domain.history.HistoryItem

class TranslateUseCase(
  private val client: TranslateClient,
  private val historyDataSource: HistoryDataSource,
) {

  suspend operator fun invoke(
    fromLanguageCode: Language,
    fromText: String,
    toLanguageCode: Language,
  ): Resource<String> {
    return try {
      val translatedText = client.translate(
        fromLanguageCode,
        fromText,
        toLanguageCode
      )

      historyDataSource.insertHistoryItem(
        HistoryItem(
          id = null,
          fromLanguageCode = fromLanguageCode.langCode,
          fromText = fromText,
          toLanguageCode = toLanguageCode.langCode,
          toText = translatedText,
        )
      )

      Resource.Success(translatedText)
    } catch (e: TranslateException) {
      e.printStackTrace()
      Resource.Error(e)
    }
  }
}