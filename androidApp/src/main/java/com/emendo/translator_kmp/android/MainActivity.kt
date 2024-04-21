package com.emendo.translator_kmp.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHost
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.emendo.translator_kmp.android.core.presentation.Routes
import com.emendo.translator_kmp.android.translate.presentation.AndroidTranslateViewModel
import com.emendo.translator_kmp.android.translate.presentation.TranslateScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      TranslatorTheme {
        Surface(
          modifier = Modifier.fillMaxSize(),
          color = MaterialTheme.colorScheme.background
        ) {
          TranslateRoot()
        }
      }
    }
  }
}

@Composable
fun TranslateRoot() {
  val navController = rememberNavController()
  NavHost(
    navController = navController,
    startDestination = Routes.TRANSLATE,
  ) {
    composable(Routes.TRANSLATE) {
      val viewModel = hiltViewModel<AndroidTranslateViewModel>()
      val state by viewModel.state.collectAsState()
      TranslateScreen(
        state = state,
        onEvent = viewModel::onEvent,
      )
    }
  }
}