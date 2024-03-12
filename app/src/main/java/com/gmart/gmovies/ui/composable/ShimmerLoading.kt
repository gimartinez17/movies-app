package com.gmart.gmovies.ui.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.gmart.gmovies.utils.AppConstants
import com.gmart.gmovies.utils.PreviewLayout
import com.gmart.gmovies.utils.ThemePreviews
import com.gmart.gmovies.utils.shimmerLoadingAnimation


@Composable
fun ShimmerLoading() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .shimmerLoadingAnimation()
    )
}

@ThemePreviews
@Composable
fun ShimmerPlaceholderPreview() {
    val configuration = LocalConfiguration.current
    val posterWidth = (configuration.screenWidthDp.dp) / 2
    PreviewLayout {
        Card(
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier
                .width(posterWidth)
                .clip(MaterialTheme.shapes.medium)
                .aspectRatio(AppConstants.POSTER_RATIO)
                .clickable { },
        ) {
            ShimmerLoading()
        }
    }
}