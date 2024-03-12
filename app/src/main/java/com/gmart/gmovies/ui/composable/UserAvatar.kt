package com.gmart.gmovies.ui.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.gmart.gmovies.BuildConfig
import com.gmart.gmovies.R
import com.gmart.gmovies.utils.AppDimensions

@Composable
fun UserAvatar(
    modifier: Modifier = Modifier,
    imageSize: Dp,
    path: String?,
) {
    Card(
        modifier = modifier.background(Color.Transparent),
        shape = CircleShape,
        elevation = CardDefaults.cardElevation(defaultElevation = AppDimensions.cardElevation),
    ) {
        SubcomposeAsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(BuildConfig.IMG_URL_S + path)
                .crossfade(true)
                .build(),
            contentDescription = null,
            contentScale = ContentScale.FillWidth,
            modifier = Modifier.size(imageSize),
            loading = { ShimmerLoading() },
            error = {
                Image(
                    painter = painterResource(id = R.drawable.user_placeholder),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    alignment = Alignment.BottomCenter,
                    colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.primary),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 12.dp),
                )
            }
        )
    }
}