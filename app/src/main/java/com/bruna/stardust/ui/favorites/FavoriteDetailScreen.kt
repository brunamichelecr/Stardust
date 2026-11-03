package com.bruna.stardust.ui.favorites

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.bruna.stardust.data.local.FavoriteApod
import com.bruna.stardust.util.htmlToPlainText

@Composable
fun FavoriteDetailScreen(
    favorite: FavoriteApod,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
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
            modifier = Modifier.fillMaxWidth()
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