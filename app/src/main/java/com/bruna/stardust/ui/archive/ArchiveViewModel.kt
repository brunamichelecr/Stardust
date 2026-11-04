package com.bruna.stardust.ui.archive

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruna.stardust.data.remote.ApodClient
import com.bruna.stardust.util.toApodDay
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import java.time.LocalDate

class ArchiveViewModel : ViewModel() {

    var uiState by mutableStateOf<ArchiveUiState>(ArchiveUiState.Idle)

    private var lastDate: LocalDate? = null

    fun loadDay(date: LocalDate) {
        lastDate = date
        uiState = ArchiveUiState.Loading
        viewModelScope.launch {
            uiState = try {
                val item = ApodClient.api.getApodByDay(toApodDay(date))
                ArchiveUiState.Success(item)
            } catch (e: IOException) {
                ArchiveUiState.Error("Verifique a conexão e tente novamente.")
            } catch (e: HttpException) {
                if (e.code() == 404) {
                    ArchiveUiState.NotFound
                } else {
                    ArchiveUiState.Error("Tente novamente dentro de alguns minutos.")
                }
            } catch (e: Exception) {
                ArchiveUiState.Error("Algo correu mal. Tente novamente.")
            }
        }
    }

    fun retry() {
        lastDate?.let { loadDay(it) }
    }
}