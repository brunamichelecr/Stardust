package com.bruna.stardust.ui.today

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import com.bruna.stardust.data.model.ApodItem
import com.bruna.stardust.util.htmlToPlainText
import android.content.Intent
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.ui.platform.LocalContext
import androidx.core.net.toUri
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Icon
import androidx.compose.material.icons.filled.Favorite
import com.bruna.stardust.ui.favorites.FavoritesViewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.foundation.layout.Arrangement
import com.bruna.stardust.ui.components.LoadingState
import com.bruna.stardust.ui.components.MessageState
import com.bruna.stardust.ui.components.TranslatableText

@Composable
fun TodayScreen(
    modifier: Modifier = Modifier,
    viewModel: TodayViewModel = viewModel(),
    favoritesViewModel: FavoritesViewModel = viewModel()
) {
    val favoriteDates by favoritesViewModel.favoriteDates.collectAsState()

    when (val state = viewModel.uiState) {
        is TodayUiState.Loading -> LoadingState(modifier = modifier)
        is TodayUiState.Success -> ApodContent(
            item = state.item,
            isFavorite = state.item.date in favoriteDates,
            onToggleFavorite = { favoritesViewModel.toggleFavorite(state.item) },
            modifier = modifier
        )
        is TodayUiState.Empty -> MessageState(
            message = "A NASA não devolveu nada hoje.",
            modifier = modifier
        )
        is TodayUiState.Error -> MessageState(
            message = state.message,
            actionLabel = "Tentar de novo",
            onAction = viewModel::load,
            modifier = modifier
        )
    }
}


@Composable
fun ApodContent(
    item: ApodItem,
    isFavorite: Boolean,
    onToggleFavorite: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Column(modifier = modifier.verticalScroll(rememberScrollState())) {
        if (item.hdurl != null) {
            AsyncImage(
                model = item.hdurl,
                contentDescription = item.title,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (item.hdurl == null) {
                Text("Sem imagem disponível.")
            }

            if (item.media_type != "image") {
                Button(onClick = {
                    context.startActivity(Intent(Intent.ACTION_VIEW, item.url.toUri()))
                }) {
                    Text("Abrir na NASA")
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        item.title,
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Text(
                        item.date,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                IconButton(onClick = onToggleFavorite) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = if (isFavorite) "Remover dos favoritos" else "Guardar nos favoritos",
                        tint = if (isFavorite) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                        }
                    )
                }
            }

            val explanation = item.explanation?.let { htmlToPlainText(it) }
            if (explanation != null) {
                TranslatableText(text = explanation)
            } else {
                Text("Sem explicação disponível.")
            }

            item.credit?.let {
                Text(
                    "Crédito: ${htmlToPlainText(it)}",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            item.copyright?.let {
                Text(
                    "© ${htmlToPlainText(it)}",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}