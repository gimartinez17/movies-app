package com.gmart.gmovies.ui.composable

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.gmart.domain.model.Detail
import com.gmart.domain.model.MediaType
import com.gmart.gmovies.utils.PreviewLayout
import com.gmart.gmovies.utils.ThemePreviews
import com.gmart.gmovies.utils.mockMovieDetails
import com.gmart.gmovies.utils.pagingItems
import com.gmart.gmovies.utils.spamItem
import kotlinx.coroutines.flow.MutableStateFlow

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PosterPagingGrid(
    modifier: Modifier = Modifier,
    title: String? = null,
    details: LazyPagingItems<Detail>,
    columns: Int,
    onMediaClick: (Int, MediaType?) -> Unit = { _, _ -> },
    listState: LazyGridState = rememberLazyGridState(),
) {
    val configuration = LocalConfiguration.current
    val posterWidth = (configuration.screenWidthDp.dp) / columns
    Box(modifier = modifier) {
        LazyVerticalGrid(
            modifier = Modifier.fillMaxSize(),
            columns = GridCells.Fixed(columns),
            verticalArrangement = Arrangement.spacedBy(6.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            state = listState,
        ) {
            if (title != null)
                spamItem {
                    Text(
                        text = title.toString(),
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }

            pagingItems(details) { item ->
                item?.let { detail ->
                    MediaPoster(
                        title = detail.title,
                        path = detail.posterPath,
                        modifier = Modifier
                            .width(posterWidth)
                            .shadow(6.dp, shape = MaterialTheme.shapes.medium, clip = false),
                        imageSize = ImageSize.MEDIUM,
                        onMediaClick = { onMediaClick(detail.id, detail.mediaType) }
                    )
                }
            }
            when (details.loadState.refresh) {
                is LoadState.Loading -> item { Loading(Modifier.fillMaxSize()) }
                else -> {}
            }
            when (details.loadState.append) {
                is LoadState.Loading -> item { Loading(Modifier.fillMaxSize()) }
                else -> {}
            }
        }
    }
}

@ThemePreviews
@Composable
private fun PosterGridPreview() {
    val moviesData = PagingData.from(List(9) { mockMovieDetails })
    val moviesFlow = MutableStateFlow(moviesData)

    PreviewLayout {
        PosterPagingGrid(
            title = "Trending now",
            details = moviesFlow.collectAsLazyPagingItems(),
            columns = 3,
        )
    }
}