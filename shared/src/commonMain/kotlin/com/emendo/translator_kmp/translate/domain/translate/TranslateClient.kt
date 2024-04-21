package com.emendo.translator_kmp.translate.domain.translate

import com.emendo.translator_kmp.core.domain.language.Language

interface TranslateClient {

  suspend fun translate(fromLanguage: Language, fromText: String, toLanguage: Language): String
}