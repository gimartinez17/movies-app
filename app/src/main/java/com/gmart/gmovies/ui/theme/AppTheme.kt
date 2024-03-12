package com.gmart.gmovies.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val systemUiController = rememberSystemUiController()
    val dynamicColor = false //Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
    val colorScheme = when {
        //dynamicColor && darkTheme -> dynamicDarkColorScheme(LocalContext.current)
        //dynamicColor && !darkTheme -> dynamicLightColorScheme(LocalContext.current)
        darkTheme -> DarkColors
        else -> LightColors
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = if (darkTheme) darkTypography else lightTypography,
        shapes = AppShapes,
        content = content
    )

    val view = LocalView.current
    if (!view.isInEditMode) SideEffect {
        val window = (view.context as Activity).window
        WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
    }
}

@Composable
fun ColorScheme.isDarkMode() = this.background.luminance() <= 0.5
