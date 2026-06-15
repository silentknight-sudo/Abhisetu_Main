package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = Primary,
    onPrimary = White,
    primaryContainer = DarkInk3,
    onPrimaryContainer = PrimaryPale,
    secondary = PrimaryLight,
    onSecondary = White,
    background = DarkInk,
    onBackground = Paper,
    surface = DarkInk2,
    onSurface = Paper,
    surfaceVariant = DarkInk3,
    onSurfaceVariant = Mist,
    outline = Slate
)

private val LightColorScheme = lightColorScheme(
    primary = Primary,
    onPrimary = White,
    primaryContainer = PrimaryPale,
    onPrimaryContainer = Primary,
    secondary = PrimaryLight,
    onSecondary = White,
    background = Paper,
    onBackground = DarkInk,
    surface = White,
    onSurface = DarkInk,
    surfaceVariant = Paper,
    onSurfaceVariant = Slate,
    outline = Fog
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = false, // Forces light scheme as the screens are structured with light backgrounds
    content: @Composable () -> Unit
) {
    val colorScheme = LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
