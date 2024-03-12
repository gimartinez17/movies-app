package com.gmart.gmovies.ui.screen.home.composable

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.gmart.domain.model.Detail
import com.gmart.gmovies.R
import com.gmart.gmovies.ui.composable.ImageSize
import com.gmart.gmovies.ui.composable.MediaPoster
import com.gmart.gmovies.utils.AppConstants.POSTER_RATIO
import com.gmart.gmovies.utils.PreviewLayout
import com.gmart.gmovies.utils.ThemePreviews
import com.gmart.gmovies.utils.mockMovieDetails
import com.gmart.gmovies.utils.upperString
import kotlin.math.absoluteValue

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ExpandedMediaPage(
    modifier: Modifier = Modifier,
    page: Int = 0,
    pagerState: PagerState,
    detail: Detail,
    aspectRatio: Float = POSTER_RATIO,
    onMediaClick: (Int) -> Unit = { },
) {
    val pageOffset = ((pagerState.currentPage - page) + pagerState.currentPageOffsetFraction)
        .absoluteValue

    ConstraintLayout(modifier = modifier) {
        val (posterRef, textRef, buttonRef) = createRefs()
        MediaPoster(
            title = detail.title,
            path = detail.posterPath,
            imageSize = ImageSize.LARGE,
            aspectRatio = aspectRatio,
            modifier = modifier
                .constrainAs(posterRef) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(textRef.top)
                    width = Dimension.matchParent
                }
                .wrapContentSize()
                .padding(horizontal = 8.dp, vertical = 10.dp)
                .graphicsLayer {
                    val scale = lerp(1f, 0.90f, pageOffset)
                    alpha *= scale
                    scaleY *= scale
                }
                .shadow(6.dp, shape = MaterialTheme.shapes.medium, clip = false),
        )
        Text(
            text = if (detail.overview?.isNotBlank() == true) detail.overview!! else detail.title,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            overflow = TextOverflow.Ellipsis,
            softWrap = true,
            minLines = 1,
            maxLines = 6,
            modifier = Modifier
                .constrainAs(textRef) {
                    top.linkTo(posterRef.bottom)
                    bottom.linkTo(buttonRef.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    height = Dimension.preferredWrapContent
                }
                .padding(horizontal = 32.dp),
        )
        ElevatedButton(
            onClick = { onMediaClick(detail.id) },
            modifier = Modifier
                .constrainAs(buttonRef) {
                    top.linkTo(textRef.bottom)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .padding(top = 8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Text(
                text = stringResource(id = R.string.more_info).upperString(),
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@ThemePreviews
@Composable
private fun ExpandedMediaPagePreview() {
    PreviewLayout {
        ExpandedMediaPage(
            pagerState = rememberPagerState(pageCount = { 1 }),
            detail = mockMovieDetails,
        )
    }
}
