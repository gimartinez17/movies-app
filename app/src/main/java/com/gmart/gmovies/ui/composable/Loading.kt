package com.gmart.gmovies.ui.composable

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import com.gmart.gmovies.utils.PreviewLayout
import com.gmart.gmovies.utils.ThemePreviews

@Composable
fun Loading(
    modifier: Modifier = Modifier,
    strokeWidth: Dp = ProgressIndicatorDefaults.CircularStrokeWidth,
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator(strokeWidth = strokeWidth)
    }
}

@ThemePreviews
@Composable
fun LoadingPreview() {
    PreviewLayout {
        Loading(modifier = Modifier.fillMaxSize())
    }
}
