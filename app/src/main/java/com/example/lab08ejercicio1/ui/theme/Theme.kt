package com.example.lab08ejercicio1.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.material3.ColorScheme
import androidx.compose.ui.graphics.Color


import com.example.lab08ejercicio1.ui.theme.* private val LightColorScheme = lightColorScheme(
primary = primaryLight,
onPrimary = onPrimaryLight,
primaryContainer = primaryContainerLight,
onPrimaryContainer = onPrimaryContainerLight,

surfaceContainerLow = surfaceContainerLowLight,
error = errorLight,

background = Color.White,
surface = Color.White,
onSurface = Color.Black
)

private val DarkColorScheme = darkColorScheme(
    primary = primaryDark,
    onPrimary = onPrimaryDark,
    primaryContainer = primaryContainerDark,
    onPrimaryContainer = onPrimaryContainerDark,
    surfaceContainerLow = surfaceContainerLowDark,
    error = errorDark,
    // Otros colores por defecto...
    background = Color(0xFF121212),
    surface = Color(0xFF121212),
    onSurface = Color.White
)

@Composable
fun Lab08Ejercicio1Theme( // Usa el nombre de tu tema
    darkTheme: Boolean = isSystemInDarkTheme(),
    // ... (otras configuraciones)
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        // ... (lógica de Android 12+)
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }


    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}