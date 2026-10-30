package com.bruna.stardust.ui.today

import com.bruna.stardust.data.model.ApodItem

sealed interface TodayUiState {
    data object Loading : TodayUiState
    data class Success(val item: ApodItem) : TodayUiState
    data class Error(val message: String) : TodayUiState
    data object Empty : TodayUiState
}