package com.bruna.stardust.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bruna.stardust.data.settings.AppLanguage
import com.bruna.stardust.ui.settings.SettingsViewModel
import com.bruna.stardust.ui.translation.TranslationViewModel

@Composable
fun TranslatedText(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle = LocalTextStyle.current,
    color: Color = Color.Unspecified
) {
    val translationViewModel: TranslationViewModel = viewModel()
    val settingsViewModel: SettingsViewModel = viewModel()
    val translate = settingsViewModel.language == AppLanguage.Portuguese

    LaunchedEffect(text, translate, translationViewModel.retryTick) {
        if (translate) translationViewModel.request(text)
    }

    val shown = if (translate) translationViewModel.translatedOrNull(text) ?: text else text
    Text(text = shown, modifier = modifier, style = style, color = color)
}

@Composable
fun TranslatableText(
    text: String,
    modifier: Modifier = Modifier
) {
    val translationViewModel: TranslationViewModel = viewModel()
    val settingsViewModel: SettingsViewModel = viewModel()
    val translate = settingsViewModel.language == AppLanguage.Portuguese

    LaunchedEffect(text, translate, translationViewModel.retryTick) {
        if (translate) translationViewModel.request(text)
    }

    val translated = if (translate) translationViewModel.translatedOrNull(text) else null

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(translated ?: text)

        if (translate && translated == null) {
            if (translationViewModel.hasError) {
                Text(
                    "Não foi possível traduzir. Conecte-se à internet e tente novamente.",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.error
                )
                TextButton(onClick = translationViewModel::retry) {
                    Text("Tentar de novo")
                }
            } else {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        "A traduzir… Na primeira vez, baixa o pacote de idioma (cerca de 30 MB).",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}