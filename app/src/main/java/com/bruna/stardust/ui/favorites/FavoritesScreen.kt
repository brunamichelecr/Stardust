package com.bruna.stardust.ui.favorites

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import com.bruna.stardust.data.local.FavoriteApod
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue

@Composable
fun FavoritesScreen(
    selectedDate: String?,
    onSelectedDateChange: (String?) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: FavoritesViewModel = viewModel()
) {
    val favorites by viewModel.favorites.collectAsState()
    var searchText by rememberSaveable { mutableStateOf("") }
    val filteredFavorites = favorites.filter {
        it.title.contains(searchText, ignoreCase = true)
    }

    val selected = favorites.find { it.date == selectedDate }
    BackHandler(enabled = selected != null) {
        onSelectedDateChange(null)
    }
    if (selected != null) {
        FavoriteDetailScreen(
            favorite = selected,
            onBack = { onSelectedDateChange(null) },
            onSaveNote = { note -> viewModel.updateNote(selected.date, note) },
            modifier = modifier
        )
    } else if (favorites.isEmpty()) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Ainda não tem favoritos.")
        }
    } else {
        LazyColumn(modifier = modifier.fillMaxSize()) {
            item {
                OutlinedTextField(
                    value = searchText,
                    onValueChange = { novoTexto -> searchText = novoTexto },
                    label = { Text("Buscar nos favoritos") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
            }
            if (filteredFavorites.isEmpty()) {
                item {
                    Text(
                        "Nenhum favorito encontrado.",
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
            items(filteredFavorites, key = { it.date }) { favorite ->
                FavoriteRow(
                    favorite = favorite,
                    onClick = { onSelectedDateChange(favorite.date) },
                    onDelete = { viewModel.delete(favorite.date) })
            }
        }
    }
}

@Composable
private fun FavoriteRow(
    favorite: FavoriteApod, onClick: () -> Unit, onDelete: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = favorite.hdurl,
            contentDescription = favorite.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier.size(72.dp)
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 12.dp)
        ) {
            Text(favorite.title)
            Text(favorite.date)
        }
        IconButton(onClick = onDelete) {
            Icon(
                imageVector = Icons.Default.Delete, contentDescription = "Remover dos favoritos"
            )
        }
    }
}