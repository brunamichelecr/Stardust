package com.bruna.stardust.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector

enum class AppTab(val label: String, val icon: ImageVector) {
    Today("Hoje", Icons.Default.Home),
    Archive("Arquivo", Icons.Default.DateRange),
    Favorites("Favoritos", Icons.Default.Favorite)
}