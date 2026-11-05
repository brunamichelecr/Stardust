package com.bruna.stardust.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.bruna.stardust.data.translation.ExplanationTranslator

private sealed interface TranslationUiState {
    data object Idle : TranslationUiState
    data object Loading : TranslationUiState
    data class Success(val text: String) : TranslationUiState
    data object Error : TranslationUiState
}

@Composable
fun TranslatableText(
    text: String,
    modifier: Modifier = Modifier
) {
    val translator = remember { ExplanationTranslator() }
    DisposableEffect(Unit) {
        onDispose { translator.close() }
    }

    var state by remember(text) { mutableStateOf<TranslationUiState>(TranslationUiState.Idle) }

    fun startTranslation() {
        state = TranslationUiState.Loading
        translator.translate(
            text = text,
            onSuccess = { state = TranslationUiState.Success(it) },
            onError = { state = TranslationUiState.Error }
        )
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        val current = state
        Text(if (current is TranslationUiState.Success) current.text else text)

        when (current) {
            is TranslationUiState.Idle -> TextButton(onClick = ::startTranslation) {
                Text("Traduzir para português")
            }

            is TranslationUiState.Loading -> Row(verticalAlignment = Alignment.CenterVertically) {
                CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    "A traduzir… Na primeira vez, baixa o pacote de idioma (cerca de 30 MB).",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            is TranslationUiState.Success -> TextButton(onClick = { state = TranslationUiState.Idle }) {
                Text("Ver original")
            }

            is TranslationUiState.Error -> {
                Text(
                    "Conecte-se à internet e tente novamente.",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.error
                )
                TextButton(onClick = ::startTranslation) { Text("Tentar de novo") }
            }
        }
    }
}