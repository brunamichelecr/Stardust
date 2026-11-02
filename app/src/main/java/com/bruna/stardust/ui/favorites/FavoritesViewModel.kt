package com.bruna.stardust.ui.favorites

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.bruna.stardust.data.local.AppDatabase
import com.bruna.stardust.data.local.toFavorite
import com.bruna.stardust.data.model.ApodItem
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FavoritesViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = AppDatabase.getInstance(application).favoriteDao()

    val favoriteDates: StateFlow<Set<String>> = dao.getAll()
        .map { list -> list.map { it.date }.toSet() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptySet())

    fun toggleFavorite(item: ApodItem) {
        viewModelScope.launch {
            if (item.date in favoriteDates.value) {
                dao.delete(item.date)
            } else {
                dao.insert(item.toFavorite())
            }
        }
    }
}