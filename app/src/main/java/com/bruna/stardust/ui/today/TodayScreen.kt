package com.bruna.stardust.ui.today

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import com.bruna.stardust.data.model.ApodItem
import com.bruna.stardust.util.htmlToPlainText
import android.content.Intent
import androidx.compose.material3.Button
import androidx.compose.ui.platform.LocalContext
import androidx.core.net.toUri

@Composable
fun TodayScreen(
    modifier: Modifier = Modifier,
    viewModel: TodayViewModel = viewModel()
) {
    when (val state = viewModel.uiState) {
        is TodayUiState.Loading -> Text("Carregando...", modifier = modifier)
        is TodayUiState.Success -> ApodContent(item = state.item, modifier = modifier)
        is TodayUiState.Empty -> Text("A NASA não devolveu nada.", modifier = modifier)
        is TodayUiState.Error -> Text("Erro: ${state.message}", modifier = modifier)
    }
}

@Composable
private fun ApodContent(item: ApodItem, modifier: Modifier = Modifier) {
    val context = LocalContext.current

    Column(modifier = modifier.verticalScroll(rememberScrollState())) {
        if (item.hdurl != null) {
            AsyncImage(
                model = item.hdurl,
                contentDescription = item.title,
                modifier = Modifier.fillMaxWidth()
            )
        } else {
            Text("Sem imagem disponível.")
        }

        if (item.media_type != "image") {
            Button(onClick = {
                context.startActivity(Intent(Intent.ACTION_VIEW, item.url.toUri()))
            }) {
                Text("Abrir na NASA")
            }
        }

        Text(item.title)
        Text(item.explanation?.let { htmlToPlainText(it) } ?: "Sem explicação disponível.")
        item.credit?.let { Text("Crédito: ${htmlToPlainText(it)}") }
        item.copyright?.let { Text("© ${htmlToPlainText(it)}") }
    }
}