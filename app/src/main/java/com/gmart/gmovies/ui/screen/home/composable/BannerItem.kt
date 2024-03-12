package com.gmart.gmovies.ui.screen.home.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.gmart.domain.model.Detail
import com.gmart.gmovies.BuildConfig
import com.gmart.gmovies.ui.composable.ImageSize
import com.gmart.gmovies.ui.composable.MediaPoster
import com.gmart.gmovies.ui.composable.ShimmerLoading
import com.gmart.gmovies.utils.AppDimensions
import com.gmart.gmovies.utils.PreviewLayout
import com.gmart.gmovies.utils.ThemePreviews
import com.gmart.gmovies.utils.mockMovieDetails

@Composable
fun BannerItem(
    detail: Detail,
    modifier: Modifier = Modifier,
    shape: Shape = MaterialTheme.shapes.medium,
    onClick: (Int) -> Unit = {}
) {
    var sizeImage by remember { mutableStateOf(IntSize.Zero) }

    Card(
        shape = shape,
        elevation = CardDefaults.cardElevation(defaultElevation = AppDimensions.cardElevation),
        modifier = modifier
            .clip(shape)
            .background(Color.Transparent)
            .clickable { onClick(detail.id) }
            .aspectRatio(12 / 8f),
    ) {
        ConstraintLayout(modifier = Modifier.fillMaxSize()) {
            val (backdropRef, posterRef, titleRef, descriptionRef) = createRefs()
            val guideline = createGuidelineFromTop(0.35f)

            val backdropPath = BuildConfig.IMG_URL_S + detail.backdropPath
            SubcomposeAsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(backdropPath)
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                loading = { ShimmerLoading() },
                modifier = Modifier
                    .constrainAs(backdropRef) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                        width = Dimension.fillToConstraints
                        height = Dimension.wrapContent
                    }
                    .onGloballyPositioned {
                        sizeImage = it.size
                    },
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            startY = sizeImage.height.toFloat() / 3,
                            endY = sizeImage.height.toFloat(),
                            colors = listOf(Color.Transparent, Color.Black)
                        )
                    )
            )
            MediaPoster(
                modifier = Modifier
                    .padding(4.dp)
                    .constrainAs(posterRef) {
                        top.linkTo(guideline)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        height = Dimension.fillToConstraints
                    }
                    .shadow(6.dp, shape = MaterialTheme.shapes.medium, clip = false),
                path = detail.posterPath,
                imageSize = ImageSize.SMALL,
                onMediaClick = { onClick(detail.id) }
            )

            Text(
                text = detail.title,
                style = MaterialTheme.typography.titleMedium.copy(color = Color.White),
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .constrainAs(titleRef) {
                        start.linkTo(posterRef.end)
                        end.linkTo(parent.end)
                        bottom.linkTo(descriptionRef.top)
                        width = Dimension.fillToConstraints
                    }
                    .padding(horizontal = 4.dp)
            )
            detail.overview?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        lineHeight = 16.sp,
                        color = Color.White,
                    ),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 3,
                    modifier = Modifier
                        .wrapContentWidth()
                        .constrainAs(descriptionRef) {
                            start.linkTo(posterRef.end)
                            end.linkTo(parent.end)
                            bottom.linkTo(parent.bottom)
                            width = Dimension.fillToConstraints
                        }
                        .padding(start = 4.dp, end = 4.dp, bottom = 12.dp)
                )
            }
        }
    }
}

@ThemePreviews
@Composable
private fun NowPlayingItemPreview() {
    PreviewLayout {
        BannerItem(detail = mockMovieDetails)
    }
}