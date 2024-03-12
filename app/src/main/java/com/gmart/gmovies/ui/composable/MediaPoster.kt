package com.gmart.gmovies.ui.composable

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.constraintlayout.compose.Visibility
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.gmart.gmovies.BuildConfig.IMG_URL_S
import com.gmart.gmovies.BuildConfig.IMG_URL_XL
import com.gmart.gmovies.R
import com.gmart.gmovies.ui.theme.DarkBlueBackground
import com.gmart.gmovies.ui.theme.LightBlueBackground
import com.gmart.gmovies.ui.theme.MovieLightTertiary
import com.gmart.gmovies.ui.theme.isDarkMode
import com.gmart.gmovies.utils.AppConstants.POSTER_RATIO
import com.gmart.gmovies.utils.AppDimensions
import com.gmart.gmovies.utils.PreviewLayout
import com.gmart.gmovies.utils.ThemePreviews
import com.gmart.gmovies.utils.conditional
import com.gmart.gmovies.utils.mockMovieDetails

enum class ImageSize(val font: TextUnit = TextUnit.Unspecified) {
    SMALL(8.sp),
    MEDIUM(14.sp),
    LARGE(24.sp)
}

@Composable
fun MediaPoster(
    modifier: Modifier = Modifier,
    path: String?,
    title: String? = null,
    imageSize: ImageSize = ImageSize.MEDIUM,
    shape: Shape = MaterialTheme.shapes.medium,
    aspectRatio: Float = POSTER_RATIO,
    @DrawableRes placeholderId: Int = R.drawable.placeholder_poster,
    onMediaClick: (() -> Unit)? = null,
) {
    Card(
        shape = shape,
        elevation = CardDefaults.cardElevation(defaultElevation = AppDimensions.cardElevation),
        modifier = modifier
            .clip(shape)
            .background(MaterialTheme.colorScheme.surface)
            .aspectRatio(aspectRatio)
            .shadow(6.dp, shape = MaterialTheme.shapes.medium, clip = false)
            .conditional(onMediaClick != null, { clickable { onMediaClick!!() } })
    ) {
        val baseUrl = if (imageSize == ImageSize.LARGE) IMG_URL_XL else IMG_URL_S
        val posterUrl = baseUrl + path
        SubcomposeAsyncImage(
            modifier = Modifier.fillMaxSize(),
            model = ImageRequest.Builder(LocalContext.current)
                .data(posterUrl)
                .crossfade(true)
                .build(),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            loading = { ShimmerLoading() },
            error = {
                PosterPlaceholder(
                    mediaTitle = title,
                    imageSize = imageSize,
                    placeholderId = placeholderId,
                )
            }
        )
    }
}

@Composable
private fun PosterPlaceholder(
    mediaTitle: String? = null,
    imageSize: ImageSize,
    @DrawableRes placeholderId: Int,
) {
    val isDarkMode = MaterialTheme.colorScheme.isDarkMode()
    val background = if (isDarkMode) DarkBlueBackground else LightBlueBackground
    val brush = if (isDarkMode) Color.Transparent else MovieLightTertiary
    val surface = MaterialTheme.colorScheme.surface

    Box(modifier = Modifier.background(surface)) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .background(brush = Brush.verticalGradient(colors = listOf(background, brush))),
        ) {
            val (imageRef, titleRef) = createRefs()
            Image(
                painter = painterResource(id = placeholderId),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                alignment = Alignment.Center,
                alpha = 0.6f,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(if (imageSize == ImageSize.LARGE) 32.dp else 12.dp)
                    .constrainAs(imageRef) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(titleRef.top)
                        height = Dimension.fillToConstraints
                    },
            )

            Text(
                text = (mediaTitle ?: "").uppercase(),
                modifier = Modifier
                    .constrainAs(titleRef) {
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        visibility = if (mediaTitle != null) {
                            Visibility.Visible
                        } else {
                            Visibility.Gone
                        }
                    }
                    .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
                textAlign = TextAlign.Center,
                fontSize = imageSize.font,
                lineHeight = imageSize.font,
                style = MaterialTheme.typography.labelLarge.copy(
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold
                ),
                maxLines = 4,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@ThemePreviews
@Composable
fun LargeMoviePosterPreview(
) {
    val configuration = LocalConfiguration.current
    val posterWidth = (configuration.screenWidthDp.dp) * 3 / 4
    PreviewLayout {
        Card(
            shape = MaterialTheme.shapes.medium,
            elevation = CardDefaults.cardElevation(defaultElevation = AppDimensions.cardElevation),
            modifier = Modifier
                .width(posterWidth)
                .clip(MaterialTheme.shapes.medium)
                .aspectRatio(POSTER_RATIO)
                .shadow(6.dp, shape = MaterialTheme.shapes.medium, clip = false)
                .clickable { },
        ) {
            PosterPlaceholder(
                mediaTitle = mockMovieDetails.title.uppercase(),
                imageSize = ImageSize.LARGE,
                placeholderId = R.drawable.placeholder_poster
            )
        }
    }
}

@ThemePreviews
@Composable
fun SmallPersonPreview(
) {
    val configuration = LocalConfiguration.current
    val posterWidth = (configuration.screenWidthDp.dp) / 3
    PreviewLayout {
        Card(
            shape = MaterialTheme.shapes.medium,
            elevation = CardDefaults.cardElevation(defaultElevation = AppDimensions.cardElevation),
            modifier = Modifier
                .width(posterWidth)
                .clip(MaterialTheme.shapes.medium)
                .aspectRatio(POSTER_RATIO)
                .shadow(6.dp, shape = MaterialTheme.shapes.medium, clip = false)
                .clickable { },
        ) {
            PosterPlaceholder(
                imageSize = ImageSize.SMALL,
                placeholderId = R.drawable.user_placeholder,
            )
        }
    }
}
