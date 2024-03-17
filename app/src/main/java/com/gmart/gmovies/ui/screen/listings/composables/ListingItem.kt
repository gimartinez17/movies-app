package com.gmart.gmovies.ui.screen.listings.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.constraintlayout.compose.Visibility.Companion.Gone
import androidx.constraintlayout.compose.Visibility.Companion.Visible
import com.gmart.gmovies.ui.composable.MediaBackdrop
import com.gmart.gmovies.ui.composable.ImageSize
import com.gmart.gmovies.ui.screen.details.composables.header.composables.Score
import com.gmart.gmovies.utils.AppDimensions
import com.gmart.gmovies.utils.PreviewLayout
import com.gmart.gmovies.utils.ThemePreviews
import com.gmart.gmovies.utils.mockMovieDetails

@Composable
fun ListingItem(
    modifier: Modifier = Modifier,
    title: String,
    backdropPath: String?,
    score: Float,
    onClick: () -> Unit = { },
) {
    Card(
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = AppDimensions.cardElevation),
        modifier = modifier
            .clip(MaterialTheme.shapes.medium)
            .clickable { onClick() }
            .aspectRatio(16 / 6f)
            .background(MaterialTheme.colorScheme.surface),
    ) {
        ConstraintLayout(modifier = Modifier.fillMaxSize()) {
            val (backgroundRef, titleRef, blurRef) = createRefs()
            MediaBackdrop(
                modifier = Modifier
                    .fillMaxSize()
                    .constrainAs(backgroundRef) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                    },
                path = backdropPath,
                imageSize = ImageSize.MEDIUM,
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .constrainAs(blurRef) {
                        top.linkTo(titleRef.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                        visibility =
                            if (backdropPath?.isNotBlank() == true) Visible else Gone
                        height = Dimension.fillToConstraints
                    }
                    .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.4f))
            )
            Column(modifier = Modifier
                .padding(start = 16.dp, end = 16.dp, bottom = 16.dp, top = 8.dp)
                .constrainAs(titleRef) {
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                    height = Dimension.wrapContent
                }
                .zIndex(1f)
            ) {
                Text(
                    modifier = Modifier,
                    text = title,
                    fontSize = 16.sp,
                    lineHeight = 17.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Start,
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = MaterialTheme.colorScheme.onSurface,
                        shadow = Shadow(
                            color = MaterialTheme.colorScheme.surface.copy(alpha = 0.35f),
                            offset = Offset(0f, 0f),
                            blurRadius = 4f
                        )
                    )
                )
                Spacer(modifier = Modifier.width(16.dp))
                Score(score = score, showTextScore = false)
            }
        }
    }
}

@ThemePreviews
@Composable
fun ListingItemPreview() {
    PreviewLayout {
        ListingItem(
            title = mockMovieDetails.title,
            backdropPath = mockMovieDetails.backdropPath,
            score = mockMovieDetails.voteAverage,
        )
    }
}