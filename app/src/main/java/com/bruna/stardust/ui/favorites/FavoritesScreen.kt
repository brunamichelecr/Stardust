package com.bruna.stardust.ui.favorites

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import com.bruna.stardust.data.local.FavoriteApod
import com.bruna.stardust.ui.components.MessageState
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import com.bruna.stardust.ui.components.TranslatedText

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
        MessageState(
            message = "Ainda não tem favoritos.\nToque no coração de uma foto para guardá-la.",
            modifier = modifier
        )
    } else {
        LazyColumn(modifier = modifier.fillMaxSize()) {
            item {
                OutlinedTextField(
                    value = searchText,
                    onValueChange = { novoTexto -> searchText = novoTexto },
                    label = { Text("Buscar nos favoritos") },
                    singleLine = true,
                    shape = RoundedCornerShape(24.dp),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null
                        )
                    },
                    trailingIcon = if (searchText.isNotEmpty()) {
                        {
                            IconButton(onClick = { searchText = "" }) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Limpar busca"
                                )
                            }
                        }
                    } else {
                        null
                    },
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
    Card(
        onClick = onClick,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = favorite.hdurl,
                contentDescription = favorite.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(72.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 12.dp)
            ) {
                TranslatedText(
                    text = favorite.title,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    favorite.date,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Remover dos favoritos"
                )
            }
        }
    }
}