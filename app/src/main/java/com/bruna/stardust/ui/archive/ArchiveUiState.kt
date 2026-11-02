package com.bruna.stardust.ui.archive

import com.bruna.stardust.data.model.ApodItem

sealed interface ArchiveUiState {
    data object Idle : ArchiveUiState
    data object Loading : ArchiveUiState
    data class Success(val item: ApodItem) : ArchiveUiState
    data object NotFound : ArchiveUiState
    data class Error(val message: String) : ArchiveUiState
}