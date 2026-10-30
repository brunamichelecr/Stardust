package com.bruna.stardust.ui.today

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruna.stardust.data.remote.ApodClient
import kotlinx.coroutines.launch

class TodayViewModel : ViewModel() {

    var uiState by mutableStateOf<TodayUiState>(TodayUiState.Loading)

    init {
        load()
    }

    private fun load() {
        viewModelScope.launch {
            uiState = try {
                val item = ApodClient.api.getApod(perPage = 1).firstOrNull()
                if (item != null) {
                    TodayUiState.Success(item)
                } else {
                    TodayUiState.Empty
                }
            } catch (e: Exception) {
                TodayUiState.Error(e.message ?: "Erro desconhecido")
            }
        }
    }
}