package com.gmart.gmovies.ui.screen.details.composables.body.composables

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.gmart.gmovies.BuildConfig

@Composable
fun ProviderIcon(logoPath: String, itemSize: Dp) {
    Card(
        shape = RoundedCornerShape(8.0.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(BuildConfig.LOGO_URL + logoPath)
                .build(),
            contentDescription = null,
            contentScale = ContentScale.FillHeight,
            modifier = Modifier
                .width(itemSize)
                .height(itemSize)
        )
    }
}