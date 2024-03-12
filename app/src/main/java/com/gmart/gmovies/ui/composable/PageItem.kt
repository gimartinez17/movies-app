package com.gmart.gmovies.ui.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.gmart.domain.model.Detail
import com.gmart.gmovies.BuildConfig
import com.gmart.gmovies.ui.screen.details.composables.header.composables.HeaderInformation
import com.gmart.gmovies.utils.PreviewLayout
import com.gmart.gmovies.utils.ThemePreviews
import com.gmart.gmovies.utils.getHoursAndMinutes
import com.gmart.gmovies.utils.getYear
import com.gmart.gmovies.utils.joinWithComa
import com.gmart.gmovies.utils.mockMovieDetails

@Composable
fun PageItem(
    modifier: Modifier = Modifier,
    detail: Detail,
    onMediaClick: (Int) -> Unit = {},
) {
    var sizeImage by remember { mutableStateOf(IntSize.Zero) }
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val headerHeight = (configuration.screenHeightDp.dp) / 2
    val headerHeightFloat = with(LocalDensity.current) { headerHeight.toPx() }

    Column {
        ConstraintLayout(
            modifier = modifier
                .height(headerHeight)
                .background(Color.Black)
                .fillMaxSize()
        ) {
            val (backdropRef, titleRef, brushRef) = createRefs()

            val backdropPath = BuildConfig.IMG_URL_XL + detail.backdropPath
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(backdropPath)
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.FillHeight,
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
                            colors = listOf(
                                Color.Transparent,
                                MaterialTheme.colorScheme.surface
                            ),
                            startY = headerHeightFloat / 3,
                            endY = headerHeightFloat
                        )
                    )
                    .constrainAs(brushRef) {
                        top.linkTo(backdropRef.top)
                        start.linkTo(backdropRef.start)
                        end.linkTo(backdropRef.end)
                        bottom.linkTo(backdropRef.bottom)
                    }
            )
            Column(
                modifier = Modifier
                    .constrainAs(titleRef) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                        width = Dimension.fillToConstraints
                    }
                    .padding(16.dp)
            ) {
                Text(
                    text = detail.title,
                    style = MaterialTheme.typography.titleLarge.copy(lineHeight = 22.sp),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                HeaderInformation(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 2.dp, bottom = 4.dp),
                    info = listOf(
                        detail.releaseDate?.getYear(context),
                        detail.genres?.map { it.name }?.joinWithComa(2),
                        detail.runtime?.getHoursAndMinutes(),
                    ),
                )
            }
        }
        ElevatedButton(
            onClick = { onMediaClick(detail.id) },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(text = "MORE DETAILS")
        }
    }
}

@ThemePreviews
@Composable
private fun PageItemPreview() {
    PreviewLayout {
        PageItem(detail = mockMovieDetails)
    }
}