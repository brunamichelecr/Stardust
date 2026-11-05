package com.bruna.stardust.ui.settings

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import com.bruna.stardust.data.settings.AppLanguage
import com.bruna.stardust.data.settings.SettingsRepository

class SettingsViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = SettingsRepository(application)

    var language by mutableStateOf(repository.load())
        private set

    fun selectLanguage(newLanguage: AppLanguage) {
        language = newLanguage
        repository.save(newLanguage)
    }
}