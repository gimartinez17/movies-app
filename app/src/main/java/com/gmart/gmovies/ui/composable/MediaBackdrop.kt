package com.gmart.gmovies.ui.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Visibility.Companion.Gone
import androidx.constraintlayout.compose.Visibility.Companion.Visible
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.gmart.gmovies.BuildConfig.IMG_URL_S
import com.gmart.gmovies.BuildConfig.IMG_URL_XL
import com.gmart.gmovies.R
import com.gmart.gmovies.ui.theme.DarkBlueBackground
import com.gmart.gmovies.ui.theme.DarkSurface
import com.gmart.gmovies.ui.theme.LightBlueBackground
import com.gmart.gmovies.ui.theme.MovieLightTertiary
import com.gmart.gmovies.ui.theme.isDarkMode
import com.gmart.gmovies.utils.AppConstants
import com.gmart.gmovies.utils.AppDimensions
import com.gmart.gmovies.utils.conditional

@Composable
fun MediaBackdrop(
    modifier: Modifier = Modifier,
    path: String?,
    title: String? = null,
    imageSize: ImageSize = ImageSize.MEDIUM,
) {
    var size by remember { mutableStateOf(IntSize.Zero) }
    val baseUrl = if (imageSize == ImageSize.LARGE) IMG_URL_XL else IMG_URL_S
    val posterUrl = baseUrl + path
    ConstraintLayout(modifier = modifier) {
        val (image, titleRef, brush) = createRefs()
        SubcomposeAsyncImage(
            modifier = Modifier
                .fillMaxSize()
                .constrainAs(image) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                }
                .onSizeChanged { size = it },
            model = ImageRequest.Builder(LocalContext.current)
                .data(posterUrl)
                .crossfade(true)
                .build(),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            loading = { ShimmerLoading() },
            error = { BackdropPlaceholder() }
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .constrainAs(brush) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                    visibility = if (title != null) Visible else Gone
                }
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color.Transparent, DarkSurface),
                        startY = size.height.toFloat() / 3,
                        endY = size.height.toFloat()
                    )
                )
        )
        Text(
            text = (title ?: "").uppercase(),
            modifier = Modifier
                .constrainAs(titleRef) {
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    visibility = if (title != null) Visible else Gone
                }
                .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
            textAlign = TextAlign.Center,
            fontSize = imageSize.font,
            lineHeight = imageSize.font,
            style = MaterialTheme.typography.labelLarge.copy(
                color = Color.White,
                fontWeight = FontWeight.Bold
            ),
            maxLines = 4,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
fun MediaBackdrop(
    modifier: Modifier = Modifier,
    path: String?,
    title: String? = null,
    imageSize: ImageSize = ImageSize.MEDIUM,
    shape: Shape = MaterialTheme.shapes.medium,
    onMediaClick: (() -> Unit)? = null
) {
    Card(
        shape = shape,
        elevation = CardDefaults.cardElevation(defaultElevation = AppDimensions.cardElevation),
        modifier = modifier
            .clip(shape)
            .aspectRatio(AppConstants.BACKDROP_RATIO)
            .background(MaterialTheme.colorScheme.surface)
            .shadow(6.dp, shape = MaterialTheme.shapes.medium, clip = false)
            .conditional(onMediaClick != null, { clickable { onMediaClick!!() } })
    ) {
        val baseUrl = if (imageSize == ImageSize.LARGE) IMG_URL_XL else IMG_URL_S
        val posterUrl = baseUrl + path
        Box {
            SubcomposeAsyncImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(AppConstants.BACKDROP_RATIO),
                model = ImageRequest.Builder(LocalContext.current)
                    .data(posterUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                loading = { ShimmerLoading() },
                error = { BackdropPlaceholder() }
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(Color.Transparent, DarkSurface),
                            startY = 0f,
                            endY = Float.POSITIVE_INFINITY
                        )
                    )
            )
            Text(
                text = (title ?: "").uppercase(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp, horizontal = 32.dp)
                    .align(Alignment.BottomCenter),
                textAlign = TextAlign.Center,
                fontSize = imageSize.font,
                lineHeight = imageSize.font,
                style = MaterialTheme.typography.labelLarge.copy(
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                ),
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Composable
fun BackdropPlaceholder(
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.FillWidth,
) {
    val isDarkMode = MaterialTheme.colorScheme.isDarkMode()
    val background = if (isDarkMode) DarkBlueBackground else LightBlueBackground
    val brush = if (isDarkMode) Color.Transparent else MovieLightTertiary
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(brush = Brush.verticalGradient(colors = listOf(background, brush)))
    ) {
        Image(
            painter = painterResource(id = R.drawable.backdrop_placeholder),
            contentDescription = null,
            contentScale = contentScale,
            alignment = Alignment.Center,
            alpha = 0.6f,
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer(compositingStrategy = CompositingStrategy.Offscreen)
                .drawWithContent {
                    val colors = listOf(Color.Black, Color.Transparent)
                    drawContent()
                    drawRect(
                        brush = Brush.verticalGradient(colors),
                        blendMode = BlendMode.DstIn
                    )
                },
        )
    }
}
