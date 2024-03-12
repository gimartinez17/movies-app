package com.gmart.gmovies.utils

import androidx.compose.ui.unit.IntSize

fun maxOf(a: IntSize, b: IntSize): IntSize {
    return if (a.height > b.height) a else b
}