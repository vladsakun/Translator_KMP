package com.emendo.translator_kmp.android.translate.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.emendo.translator_kmp.android.core.theme.LightBlue
import com.emendo.translator_kmp.core.presentation.UiLanguage
import com.emendo.translator_kmp.android.R as R

@Composable
fun LanguageDropDown(
  language: UiLanguage,
  isOpen: Boolean,
  onClick: () -> Unit,
  onDismiss: () -> Unit,
  onSelectLanguage: (UiLanguage) -> Unit,
  modifier: Modifier = Modifier,
) {
  Box(modifier = modifier) {
    DropdownMenu(
      expanded = isOpen,
      onDismissRequest = onDismiss,
    ) {
      UiLanguage.allLanguages.forEach { language ->
        LanguageDropDownItem(
          language = language,
          onClick = {
            onSelectLanguage(language)
            onDismiss()
          },
          modifier = Modifier.fillMaxWidth(),
        )
      }
    }

    Row(
      modifier = Modifier
        .clickable(onClick = onClick)
        .padding(16.dp),
      verticalAlignment = Alignment.CenterVertically,
    ) {
      AsyncImage(
        model = language.drawableRes,
        contentDescription = language.language.langName,
        modifier = Modifier.size(30.dp),
      )
      Spacer(modifier = Modifier.width(16.dp))
      Text(
        text = language.language.langName,
        color = LightBlue,
      )
      Icon(
        imageVector = if (isOpen) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
        contentDescription = if (isOpen) stringResource(id = R.string.close) else stringResource(id = R.string.open),
        tint = LightBlue,
      )
    }
  }
}