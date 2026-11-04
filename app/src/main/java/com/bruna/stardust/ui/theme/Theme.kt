package com.bruna.stardust.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val StardustColorScheme = darkColorScheme(
    primary = NebulaLavender,
    onPrimary = DeepViolet,
    secondary = StarGold,
    onSecondary = DeepViolet,
    tertiary = CometCyan,
    onTertiary = DeepViolet,
    background = SpaceBlack,
    onBackground = MoonWhite,
    surface = SpaceSurface,
    onSurface = MoonWhite,
    surfaceVariant = SpaceSurfaceHigh,
    onSurfaceVariant = DustGrey,
    surfaceContainer = SpaceSurface
)

@Composable
fun StardustTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = StardustColorScheme,
        typography = Typography,
        content = content
    )
}