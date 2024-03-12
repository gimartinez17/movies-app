package com.gmart.gmovies.ui.screen.search

import AppBarState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.gmart.domain.model.Detail
import com.gmart.domain.model.MediaType
import com.gmart.gmovies.R
import com.gmart.gmovies.navigation.MainBarRoutes
import com.gmart.gmovies.ui.base.SIDE_EFFECTS_KEY
import com.gmart.gmovies.ui.composable.AppSearchBar
import com.gmart.gmovies.ui.screen.search.composable.SearchFilterChips
import com.gmart.gmovies.ui.screen.search.composable.SearchResultList
import com.gmart.gmovies.utils.PreviewLayout
import com.gmart.gmovies.utils.ThemePreviews
import com.gmart.gmovies.utils.mockMovieDetails
import kotlinx.coroutines.flow.MutableStateFlow

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchScreen(
    paddingValues: PaddingValues = PaddingValues(),
    onComposing: (AppBarState) -> Unit = {},
    onMediaClick: (Int, MediaType) -> Unit = { _, _ -> },
    viewModel: SearchMovieViewModel = hiltViewModel(),
) {
    val searchViewState = viewModel.searchResults.collectAsLazyPagingItems()
    val trendingViewState = viewModel.trending.collectAsLazyPagingItems()

    var query by rememberSaveable { mutableStateOf("") }
    var active by rememberSaveable { mutableStateOf(false) }
    var selectedChip by rememberSaveable { mutableStateOf(MediaType.MOVIE) }
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(SIDE_EFFECTS_KEY) {
        onComposing(
            AppBarState(key = MainBarRoutes.Search.route, showBottomBar = true, showLogo = true)
        )
    }

    SearchContent(
        query = query,
        active = active,
        searchViewState = searchViewState,
        trendingViewState = trendingViewState,
        selectedChip = selectedChip,
        onQueryChange = {
            query = it
            viewModel.setEvent(SearchEvent.SearchMedia(query, selectedChip))
        },
        onSearch = { keyboardController?.hide() },
        onActiveChange = { active = it },
        onCloseClick = {
            query = ""
            keyboardController?.hide()
            viewModel.setEvent(SearchEvent.CleanResults)
        },
        onChipSelected = { newSelection ->
            selectedChip = newSelection
            viewModel.setEvent(SearchEvent.SearchMedia(query, selectedChip))
        },
        paddingValues = paddingValues,
        onMediaClick = onMediaClick,
    )
}

@Composable
private fun SearchContent(
    query: String,
    active: Boolean,
    searchViewState: LazyPagingItems<Detail>,
    trendingViewState: LazyPagingItems<Detail>,
    selectedChip: MediaType = MediaType.MOVIE,
    onQueryChange: (String) -> Unit = { },
    onSearch: (String) -> Unit = { },
    onActiveChange: (Boolean) -> Unit = { },
    onCloseClick: () -> Unit = { },
    onChipSelected: (MediaType) -> Unit = {},
    paddingValues: PaddingValues = PaddingValues(),
    onMediaClick: (Int, MediaType) -> Unit = { _, _ -> },
) {
    Column(Modifier
        .semantics { isTraversalGroup = true }
        .zIndex(1f)
        .fillMaxSize()
        .padding(
            top = paddingValues.calculateTopPadding(),
            start = 16.dp,
            end = 16.dp,
        )
    ) {
        AppSearchBar(
            query = query,
            onQueryChange = onQueryChange,
            onSearch = onSearch,
            active = active,
            onActiveChange = onActiveChange,
            placeholder = { Text(stringResource(id = R.string.search_placeholder)) },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            trailingIcon = {
                Icon(
                    Icons.Default.Close,
                    modifier = Modifier.clickable { onCloseClick() },
                    contentDescription = null
                )
            },
            content = {
                if (query.isNotEmpty())
                    SearchFilterChips(selectedChip = selectedChip, onChipSelected = onChipSelected)

                SearchResultList(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(
                            top = 4.dp,
                            bottom = paddingValues.calculateBottomPadding(),
                        ),
                    data = searchViewState,
                    contentPadding = PaddingValues(bottom = 16.dp),
                    onMediaClick = { id, type -> onMediaClick(id, type ?: selectedChip) },
                )
            }
        )
        SearchResultList(
            modifier = Modifier.fillMaxSize(),
            title = "Trending now",
            data = trendingViewState,
            contentPadding = PaddingValues(
                top = 16.dp,
                bottom = paddingValues.calculateBottomPadding().plus(16.dp),
            ),
            onMediaClick = { id, type -> type?.let { onMediaClick(id, it) } },
        )
    }
}

@ThemePreviews
@Composable
private fun SearchViewScreenPreview() {
    val moviesData = PagingData.from(List(6) { mockMovieDetails })
    val moviesFlow = MutableStateFlow(moviesData)

    PreviewLayout {
        SearchContent(
            query = "",
            active = false,
            searchViewState = moviesFlow.collectAsLazyPagingItems(),
            trendingViewState = moviesFlow.collectAsLazyPagingItems()
        )
    }
}