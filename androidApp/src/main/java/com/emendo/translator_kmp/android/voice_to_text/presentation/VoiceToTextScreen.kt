package com.emendo.translator_kmp.android.voice_to_text.presentation

import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.emendo.translator_kmp.android.R
import com.emendo.translator_kmp.android.core.theme.LightBlue
import com.emendo.translator_kmp.android.voice_to_text.presentation.component.VoiceRecorderDisplay
import com.emendo.translator_kmp.voice_to_text.presentation.DisplayState
import com.emendo.translator_kmp.voice_to_text.presentation.VoiceToTextEvent
import com.emendo.translator_kmp.voice_to_text.presentation.VoiceToTextState

@Composable
fun VoiceToTextScreen(
  state: VoiceToTextState,
  languageCode: String,
  onResult: (String) -> Unit,
  onEvent: (VoiceToTextEvent) -> Unit,
) {
  val context = LocalContext.current
  val recordAudioLauncher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.RequestPermission(),
    onResult = { isGranted ->
      onEvent(
        VoiceToTextEvent.PermissionResult(
          isGranted,
          !isGranted && (context as ComponentActivity).shouldShowRequestPermissionRationale(
            android.Manifest.permission.RECORD_AUDIO
          )
        )
      )
    }
  )

  LaunchedEffect(recordAudioLauncher) {
    recordAudioLauncher.launch(android.Manifest.permission.RECORD_AUDIO)
  }

  Scaffold(
    floatingActionButtonPosition = FabPosition.Center,
    floatingActionButton = {
      Row(
        verticalAlignment = Alignment.CenterVertically,
      ) {
        FloatingActionButton(
          onClick = {
            if (state.displayState != DisplayState.DISPLAYING_RESULTS) {
              onEvent(VoiceToTextEvent.ToggleRecording(languageCode))
            } else {
              onResult(state.spokenText)
            }
          },
          modifier = Modifier.size(75.dp),
          contentColor = MaterialTheme.colorScheme.onPrimary,
          containerColor = MaterialTheme.colorScheme.primary,
        ) {
          AnimatedContent(targetState = state.displayState) { displayState ->
            when (displayState) {
              DisplayState.SPEAKING -> {
                Icon(
                  imageVector = Icons.Rounded.Close,
                  contentDescription = stringResource(id = R.string.stop_recording),
                  modifier = Modifier.size(50.dp)
                )
              }

              DisplayState.DISPLAYING_RESULTS -> {
                Icon(
                  imageVector = Icons.Rounded.Check,
                  contentDescription = stringResource(id = R.string.apply),
                  modifier = Modifier.size(50.dp)
                )
              }

              else -> {
                Icon(
                  imageVector = ImageVector.vectorResource(id = R.drawable.mic),
                  contentDescription = stringResource(id = R.string.record_audio),
                  modifier = Modifier.size(50.dp)
                )
              }
            }
          }
        }
      }

      if (state.displayState == DisplayState.DISPLAYING_RESULTS) {
        IconButton(
          onClick = { onEvent(VoiceToTextEvent.ToggleRecording(languageCode)) }
        ) {
          Icon(
            imageVector = Icons.Rounded.Refresh,
            contentDescription = stringResource(id = R.string.record_again),
            tint = LightBlue,
          )
        }
      }
    }
  ) { paddingValues ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues)
    ) {
      Box(modifier = Modifier.fillMaxWidth()) {
        IconButton(
          onClick = { onEvent(VoiceToTextEvent.Close) },
          modifier = Modifier.align(Alignment.CenterStart),
        ) {
          Icon(
            imageVector = Icons.Rounded.Close,
            contentDescription = stringResource(id = R.string.close),
          )
        }

        if (state.displayState == DisplayState.SPEAKING) {
          Text(
            text = stringResource(id = R.string.listening),
            color = LightBlue,
            modifier = Modifier.align(Alignment.Center),
          )
        }
      }

      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(16.dp)
          .padding(bottom = 100.dp)
          .weight(1f)
          .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
      ) {
        AnimatedContent(targetState = state.displayState) { displayState ->
          when (displayState) {
            DisplayState.WAITING_TO_TALK -> {
              Text(
                text = stringResource(id = R.string.start_talking),
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center,
              )
            }

            DisplayState.SPEAKING -> {
              VoiceRecorderDisplay(
                powerRation = state.powerRatios,
                modifier = Modifier
                  .fillMaxWidth()
                  .height(100.dp),
              )
            }

            DisplayState.DISPLAYING_RESULTS -> {
              Text(
                text = state.spokenText,
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center,
              )
            }

            DisplayState.ERROR -> {
              Text(
                text = state.recordError ?: "Unknown error",
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.error,
              )
            }

            else -> Unit
          }
        }
      }
    }
  }
}