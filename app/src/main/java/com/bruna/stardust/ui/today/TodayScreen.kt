package com.bruna.stardust.ui.today

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import com.bruna.stardust.data.model.ApodItem

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
    Column(modifier = modifier) {
        if (item.hdurl != null) {
            AsyncImage(
                model = item.hdurl,
                contentDescription = item.title,
                modifier = Modifier.fillMaxWidth()
            )
        } else {
            Text("Sem imagem disponível.")
        }
        Text(item.title)
    }
}