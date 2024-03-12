package com.gmart.gmovies.utils

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.graphics.drawable.toBitmap
import androidx.core.graphics.toColor
import androidx.palette.graphics.Palette
import coil.compose.AsyncImagePainter
import coil.request.ImageRequest


fun String.getImageRequest(context: Context): ImageRequest {
    return ImageRequest.Builder(context)
        .data(this)
        .allowHardware(false)
        .build()
}

fun AsyncImagePainter.getDrawable(): Drawable? =
    (this.state as? AsyncImagePainter.State.Success)?.result?.drawable

fun Drawable.getPalette(height: Int): Palette {
    val bitmap = this.toBitmap()
    return Palette.Builder(bitmap)
        .resizeBitmapArea(0)
        .maximumColorCount(3)
        .clearFilters()
        .setRegion(0, 0, bitmap.width, height)
        .generate()
}

fun Drawable.calculateContrastColor(isDarkMode: Boolean, height: Int): Boolean {
    val palette = this.getPalette(height)
    val dominantColor = palette.getDominantColor(Color.Transparent.toArgb()).toColor()
    val red = dominantColor.red()
    val green = dominantColor.green()
    val blue = dominantColor.blue()
    val luminance = red * 0.299 + green * 0.587 + blue * 0.114
    return if (luminance < 0.33) !isDarkMode else isDarkMode
}
