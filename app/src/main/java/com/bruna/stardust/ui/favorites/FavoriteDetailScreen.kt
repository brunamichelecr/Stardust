package com.bruna.stardust.ui.favorites

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.bruna.stardust.data.local.FavoriteApod
import com.bruna.stardust.util.htmlToPlainText
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntSize

@Composable
fun FavoriteDetailScreen(
    favorite: FavoriteApod,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var fullscreen by rememberSaveable { mutableStateOf(false) }

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
                .onSizeChanged { size = it }
        ) {
            AsyncImage(
                model = favorite.hdurl,
                contentDescription = favorite.title,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer(
                        scaleX = scale,
                        scaleY = scale,
                        translationX = offset.x,
                        translationY = offset.y
                    )
                    .transformable(state = transformState)
            )
            Button(
                onClick = { fullscreen = false },
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(16.dp)
            ) {
                Text("Fechar")
            }
        }
    } else {
        Column(modifier = modifier.verticalScroll(rememberScrollState())) {
            Button(
                onClick = onBack,
                modifier = Modifier.padding(16.dp)
            ) {
                Text("Voltar")
            }

            AsyncImage(
                model = favorite.hdurl,
                contentDescription = favorite.title,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { fullscreen = true }
            )

            Column(modifier = Modifier.padding(16.dp)) {
                Text(favorite.title)
                Text(favorite.date)
                Text(
                    favorite.explanation?.let { htmlToPlainText(it) }
                        ?: "Sem explicação disponível."
                )
            }
        }
    }
}