package com.gmart.gmovies.ui.screen.explorer

import AppBarState
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.gmart.domain.model.Detail
import com.gmart.domain.model.MediaListType
import com.gmart.domain.model.MediaType
import com.gmart.gmovies.navigation.ScreenRoutes
import com.gmart.gmovies.ui.base.SIDE_EFFECTS_KEY
import com.gmart.gmovies.ui.composable.PosterPagingGrid
import com.gmart.gmovies.utils.DeviceScreenConfiguration
import com.gmart.gmovies.utils.DeviceScreenConfiguration.DeviceScreenSize.ExtraSmall
import com.gmart.gmovies.utils.DeviceScreenConfiguration.DeviceScreenSize.Large
import com.gmart.gmovies.utils.DeviceScreenConfiguration.DeviceScreenSize.Medium
import com.gmart.gmovies.utils.DeviceScreenConfiguration.DeviceScreenSize.Small
import com.gmart.gmovies.utils.PreviewLayout
import com.gmart.gmovies.utils.ThemePreviews
import com.gmart.gmovies.utils.mockMovieDetails
import com.gmart.gmovies.utils.rememberScreenConfiguration
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun ExplorerScreen(
    mediaListType: MediaListType?,
    paddingValues: PaddingValues = PaddingValues(),
    onComposing: (AppBarState) -> Unit = {},
    onMediaClick: (Int, MediaType) -> Unit = { _, _ -> },
    viewModel: ExplorerViewModel = hiltViewModel(),
    configuration: DeviceScreenConfiguration = rememberScreenConfiguration(),
) {
    val explorerViewState = viewModel.viewState.collectAsLazyPagingItems()
    val explorerEffect by viewModel.effect.collectAsStateWithLifecycle(ExplorerEffect.None)

    LaunchedEffect(SIDE_EFFECTS_KEY) { onComposing(explorerAppBar(mediaListType)) }

    val items by remember(key1 = configuration.size) {
        mutableIntStateOf(
            when (configuration.size) {
                ExtraSmall, Small -> 2
                Medium, Large -> 4
            }
        )
    }

    ExplorerContent(
        modifier = Modifier.fillMaxSize(),
        title = mediaListType?.title,
        details = explorerViewState,
        columns = items,
        onClick = { id, type -> viewModel.setEvent(ExplorerEvent.OnMediaClick(id, type)) },
        paddingValues = paddingValues,
    )

    LaunchedEffect(explorerEffect) {
        when (val effect = explorerEffect) {
            ExplorerEffect.None -> {}
            is ExplorerEffect.NavigateToDetails -> onMediaClick(effect.id, effect.mediaType)
        }
    }
}

private fun explorerAppBar(mediaListType: MediaListType?) = AppBarState(
    key = ScreenRoutes.Explorer.route + mediaListType?.title,
    showBackAction = true,
    showLogo = true,
)

@Composable
fun ExplorerContent(
    modifier: Modifier = Modifier,
    title: String? = null,
    details: LazyPagingItems<Detail>,
    columns: Int,
    onClick: (Int, MediaType?) -> Unit = { _, _ -> },
    paddingValues: PaddingValues = PaddingValues(),
) {
    if (details.itemCount > 0) PosterPagingGrid(
        modifier = modifier.padding(
            top = paddingValues.calculateTopPadding().plus(8.dp),
            start = 16.dp,
            end = 16.dp
        ),
        title = title,
        details = details,
        columns = columns,
        onMediaClick = onClick,
        listState = rememberLazyGridState(),
    )
}

@ThemePreviews
@Composable
fun ExplorerContentPreview() {
    val moviesData = PagingData.from(List(9) { mockMovieDetails })
    val moviesFlow = MutableStateFlow(moviesData)

    PreviewLayout {
        ExplorerContent(
            title = "Explore",
            details = moviesFlow.collectAsLazyPagingItems(),
            columns = 3
        )
    }
}
