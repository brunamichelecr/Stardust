package com.bruna.stardust.ui.favorites

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.bruna.stardust.data.local.FavoriteApod
import com.bruna.stardust.util.htmlToPlainText
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.IconButtonDefaults
import android.content.Intent
import androidx.compose.ui.platform.LocalContext
import androidx.core.net.toUri

@Composable
fun FavoriteDetailScreen(
    favorite: FavoriteApod,
    onBack: () -> Unit,
    onSaveNote: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var fullscreen by rememberSaveable { mutableStateOf(false) }
    var noteText by rememberSaveable { mutableStateOf(favorite.note) }
    val context = LocalContext.current

    BackHandler(enabled = fullscreen) {
        fullscreen = false
    }

    if (fullscreen) {
        var scale by remember { mutableFloatStateOf(1f) }
        var offset by remember { mutableStateOf(Offset.Zero) }
        var size by remember { mutableStateOf(IntSize.Zero) }
        val transformState = rememberTransformableState { zoomChange, offsetChange, _ ->
            scale = (scale * zoomChange).coerceIn(1f, 5f)
            val maxX = size.width * (scale - 1f) / 2f
            val maxY = size.height * (scale - 1f) / 2f
            offset = Offset(
                x = (offset.x + offsetChange.x).coerceIn(-maxX, maxX),
                y = (offset.y + offsetChange.y).coerceIn(-maxY, maxY)
            )
        }

        Box(
            modifier = modifier
                .fillMaxSize()
                .background(Color.Black)
                .onSizeChanged { size = it }) {
            AsyncImage(
                model = favorite.hdurl,
                contentDescription = favorite.title,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxSize()
                    .clickable { fullscreen = false }
                    .graphicsLayer(
                        scaleX = scale,
                        scaleY = scale,
                        translationX = offset.x,
                        translationY = offset.y
                    )
                    .transformable(state = transformState))
            IconButton(
                onClick = { fullscreen = false },
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = Color.Black.copy(alpha = 0.5f),
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Fechar"
                )
            }
        }
    } else {
        Column(
            modifier = modifier
                .imePadding()
                .verticalScroll(rememberScrollState())
        ) {
            IconButton(
                onClick = onBack,
                modifier = Modifier.padding(8.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Voltar"
                )
            }

            AsyncImage(
                model = favorite.hdurl,
                contentDescription = favorite.title,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { fullscreen = true })
            if (favorite.mediaType != "image") {
                Button(onClick = {
                    context.startActivity(Intent(Intent.ACTION_VIEW, favorite.url.toUri()))
                }) {
                    Text("Abrir na NASA")
                }
            }
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Column {
                    Text(
                        favorite.title, style = MaterialTheme.typography.headlineSmall
                    )
                    Text(
                        favorite.date,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Text(favorite.explanation?.let { htmlToPlainText(it) }
                    ?: "Sem explicação disponível.")
            }
            OutlinedTextField(
                value = noteText,
                onValueChange = { novoTexto -> noteText = novoTexto },
                label = { Text("A minha nota") },
                minLines = 4,
                maxLines = 4,
                trailingIcon = {
                    Column {
                        IconButton(
                            onClick = { onSaveNote(noteText) }, enabled = noteText != favorite.note
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Guardar nota"
                            )
                        }
                        IconButton(
                            onClick = {
                                noteText = ""
                                onSaveNote("")
                            }, enabled = favorite.note.isNotEmpty()
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Apagar nota"
                            )
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
        }
    }
}