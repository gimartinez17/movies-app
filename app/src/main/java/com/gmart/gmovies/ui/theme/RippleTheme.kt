package com.gmart.gmovies.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.ripple.RippleAlpha
import androidx.compose.material.ripple.RippleTheme
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

object AppRippleTheme : RippleTheme {

    @Composable
    override fun defaultColor(): Color = LocalContentColor.current

    @Composable
    override fun rippleAlpha(): RippleAlpha = RippleTheme.defaultRippleAlpha(
        contentColor = LocalContentColor.current,
        lightTheme = isSystemInDarkTheme().not()
    )
}