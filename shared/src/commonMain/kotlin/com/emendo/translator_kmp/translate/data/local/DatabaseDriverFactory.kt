package com.emendo.translator_kmp.translate.data.local

import app.cash.sqldelight.db.SqlDriver

expect class DatabaseDriverFactory {
  fun create(): SqlDriver
}