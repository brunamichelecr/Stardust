package com.bruna.stardust.ui.today

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun TodayScreen(
    modifier: Modifier = Modifier,
    viewModel: TodayViewModel = viewModel()
) {
    when (val state = viewModel.uiState) {
        is TodayUiState.Loading -> Text("Carregando...", modifier = modifier)
        is TodayUiState.Success -> Text(state.item.title, modifier = modifier)
        is TodayUiState.Empty -> Text("A NASA não devolveu nada.", modifier = modifier)
        is TodayUiState.Error -> Text("Erro: ${state.message}", modifier = modifier)
    }
}