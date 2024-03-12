package com.gmart.gmovies.ui.screen.search.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.gmart.gmovies.ui.composable.MediaBackdrop
import com.gmart.gmovies.ui.screen.details.composables.header.composables.Score
import com.gmart.gmovies.utils.AppDimensions
import com.gmart.gmovies.utils.PreviewLayout
import com.gmart.gmovies.utils.ThemePreviews
import com.gmart.gmovies.utils.getYear
import com.gmart.gmovies.utils.joinWithComa
import com.gmart.gmovies.utils.mockMovieDetails

@Composable
fun SearchResultItem(
    title: String,
    originCountry: List<String>?,
    releaseDate: String?,
    backdropPath: String?,
    score: Float,
    modifier: Modifier = Modifier,
    onMediaClick: () -> Unit = { },
) {
    val context = LocalContext.current
    Card(
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = AppDimensions.cardElevation),
        modifier = modifier.shadow(4.dp, shape = MaterialTheme.shapes.medium, clip = false),
    ) {
        ConstraintLayout(
            modifier = modifier
                .fillMaxWidth()
                .clickable { onMediaClick() }
        ) {
            val (posterRef, titleRef) = createRefs()
            MediaBackdrop(
                path = backdropPath,
                modifier = Modifier
                    .aspectRatio(14 / 8f)
                    .constrainAs(posterRef) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(titleRef.start)
                        bottom.linkTo(parent.bottom)
                    }
                    .graphicsLayer(compositingStrategy = CompositingStrategy.Offscreen)
                    .drawWithContent {
                        val colors = listOf(Color.Black, Color.Black, Color.Transparent)
                        drawContent()
                        drawRect(
                            brush = Brush.horizontalGradient(colors),
                            blendMode = BlendMode.DstIn
                        )
                    }
            )
            Column(
                modifier = Modifier
                    .constrainAs(titleRef) {
                        top.linkTo(parent.top)
                        start.linkTo(posterRef.end)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                        width = Dimension.fillToConstraints
                    }
                    .padding(horizontal = 8.dp)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium.copy(lineHeight = 18.sp),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 2,
                )
                Row {
                    Text(
                        text = releaseDate?.getYear(context) ?: "",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    if (originCountry != null && releaseDate != null) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.CenterVertically)
                                .padding(horizontal = 4.dp)
                                .size(4.dp)
                                .clip(CircleShape)
                                .background(color = MaterialTheme.typography.bodyMedium.color)
                        )
                        Text(
                            text = originCountry.map { it }.joinWithComa(3),
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    }
                }
                Score(score = score)
            }
        }
    }
}

@ThemePreviews
@Composable
private fun SearchResultItemPreview() {
    PreviewLayout {
        SearchResultItem(
            modifier = Modifier.height(120.dp),
            title = mockMovieDetails.title,
            backdropPath = mockMovieDetails.backdropPath,
            score = mockMovieDetails.voteAverage,
            originCountry = mockMovieDetails.originCountry,
            releaseDate = mockMovieDetails.releaseDate,
        )
    }
}