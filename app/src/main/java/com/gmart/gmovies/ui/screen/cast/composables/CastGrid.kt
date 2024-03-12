package com.gmart.gmovies.ui.screen.cast.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.gmart.domain.model.Cast
import com.gmart.gmovies.R
import com.gmart.gmovies.ui.composable.ImageSize
import com.gmart.gmovies.ui.composable.MediaPoster
import com.gmart.gmovies.utils.AppConstants.CAST_RATIO
import com.gmart.gmovies.utils.PreviewLayout
import com.gmart.gmovies.utils.ThemePreviews
import com.gmart.gmovies.utils.mockCastData
import com.gmart.gmovies.utils.spamItem

@Composable
fun CastGrid(
    modifier: Modifier = Modifier,
    cast: List<Cast>,
    columns: Int,
    onClick: (Int) -> Unit = { },
    listState: LazyGridState = rememberLazyGridState(),
) {
    val configuration = LocalConfiguration.current
    val posterWidth = (configuration.screenWidthDp.dp) / columns
    Box(modifier = modifier.fillMaxSize()) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(columns),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            state = listState,
        ) {
            spamItem {
                Text(
                    text = "Cast",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            items(cast.size) { index ->
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    MediaPoster(
                        path = cast[index].profilePath,
                        modifier = Modifier
                            .width(posterWidth)
                            .shadow(6.dp, shape = MaterialTheme.shapes.medium, clip = false),
                        aspectRatio = CAST_RATIO,
                        imageSize = ImageSize.MEDIUM,
                        placeholderId = R.drawable.user_placeholder,
                        onMediaClick = { onClick(cast[index].id) }
                    )

                    Text(
                        text = cast[index].name,
                        style = MaterialTheme.typography.labelMedium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = 4.dp, bottom = 2.dp)
                    )
                    Text(
                        text = cast[index].character,
                        style = MaterialTheme.typography.bodySmall,
                        textAlign = TextAlign.Center,
                        maxLines = 4,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                    )
                }
            }
        }
    }
}

@ThemePreviews
@Composable
private fun CastGridPreview() {
    PreviewLayout {
        CastGrid(
            cast = List(12) { mockCastData },
            columns = 4,
        )
    }
}