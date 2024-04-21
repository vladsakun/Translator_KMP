package com.emendo.translator_kmp.translate.data.local

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import com.emendo.translator_kmp.database.TranslatorDatabase

actual class DatabaseDriverFactory {

  actual fun create(): SqlDriver =
    NativeSqliteDriver(TranslatorDatabase.Schema, "translate.db")
}