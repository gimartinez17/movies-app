package com.gmart.gmovies.ui.screen.details.composables.header

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.gmart.gmovies.BuildConfig
import com.gmart.gmovies.ui.composable.BackdropPlaceholder
import com.gmart.gmovies.ui.composable.ShimmerLoading
import com.gmart.gmovies.ui.screen.details.composables.header.composables.HeaderTitle
import com.gmart.gmovies.ui.theme.isDarkMode
import com.gmart.gmovies.utils.AppDimensions
import com.gmart.gmovies.utils.calculateContrastColor
import com.gmart.gmovies.utils.getDrawable
import com.gmart.gmovies.utils.getImageRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


@Composable
fun DetailsHeader(
    title: String,
    imagePath: String,
    maxHeaderHeight: Dp,
    onIconComposing: (Boolean) -> Unit = {},
    extraInfo: @Composable() (ColumnScope.() -> Unit) = {},
) {
    var size by remember { mutableStateOf(IntSize.Zero) }
    val topAppBarHeight =
        with(LocalDensity.current) { AppDimensions.topAppBarHeight.toPx().toInt() }

    val isDarkMode = MaterialTheme.colorScheme.isDarkMode()
    val url = BuildConfig.IMG_URL_XL + imagePath
    val painter = rememberAsyncImagePainter(url.getImageRequest(LocalContext.current))
    var addIconBackground by remember(url) { mutableStateOf(false) }

    val drawable = painter.getDrawable()
    if (drawable != null) {
        LaunchedEffect(url) {
            withContext(Dispatchers.Default) {
                addIconBackground = drawable.calculateContrastColor(isDarkMode, topAppBarHeight)
                onIconComposing(addIconBackground)
            }
        }
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .onSizeChanged { size = it }
            .height(maxHeaderHeight)
    ) {
        Image(
            painter = painter,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .height(maxHeaderHeight)
                .fillMaxWidth()
        )
        when (painter.state) {
            is AsyncImagePainter.State.Loading -> Box(
                modifier = Modifier.fillMaxSize()
            ) { ShimmerLoading() }

            is AsyncImagePainter.State.Success -> {}
            is AsyncImagePainter.State.Error,
            AsyncImagePainter.State.Empty -> {
                BackdropPlaceholder(modifier = Modifier.fillMaxSize())
            }
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color.Transparent, MaterialTheme.colorScheme.surface),
                        startY = size.height.toFloat() / 3,
                        endY = size.height.toFloat()
                    )
                )
        )
        Column(
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.BottomCenter),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            HeaderTitle(title = title)
            extraInfo()
        }
    }
}