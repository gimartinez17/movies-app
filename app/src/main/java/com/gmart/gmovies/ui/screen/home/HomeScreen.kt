package com.gmart.gmovies.ui.screen.home

import AppBarState
import androidx.annotation.StringRes
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.gmart.domain.model.MediaListType
import com.gmart.domain.model.MediaType
import com.gmart.gmovies.R
import com.gmart.gmovies.navigation.MainBarRoutes
import com.gmart.gmovies.ui.base.SIDE_EFFECTS_KEY
import com.gmart.gmovies.ui.composable.TextTabRow
import com.gmart.gmovies.ui.screen.home.movies.MoviesScreen
import com.gmart.gmovies.ui.screen.home.tvshows.TvShowScreen
import com.gmart.gmovies.utils.AppDimensions

enum class HomePages(@StringRes val titleResId: Int) {
    MOVIES(R.string.movies),
    TV_SHOW(R.string.tv_shows)
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    paddingValues: PaddingValues,
    pages: Array<HomePages> = HomePages.values(),
    onMediaClick: (Int, MediaType) -> Unit = { _, _ -> },
    onGenreClick: (Int, MediaListType) -> Unit = { _, _ -> },
    onViewAll: (MediaListType) -> Unit = {},
    onComposing: (AppBarState) -> Unit = {},
) {
    val pagerState = rememberPagerState(pageCount = { 2 })

    LaunchedEffect(SIDE_EFFECTS_KEY) {
        onComposing(
            AppBarState(key = MainBarRoutes.Home.route, showBottomBar = true, showLogo = true)
        )
    }

    HomeContent(
        pages = pages,
        pagerState = pagerState,
        paddingValues = paddingValues,
        onMediaClick = onMediaClick,
        onGenreClick = onGenreClick,
        onViewAll = onViewAll,
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun HomeContent(
    pages: Array<HomePages>,
    pagerState: PagerState,
    paddingValues: PaddingValues = PaddingValues(),
    onMediaClick: (Int, MediaType) -> Unit = { _, _ -> },
    onGenreClick: (Int, MediaListType) -> Unit = { _, _ -> },
    onViewAll: (MediaListType) -> Unit = {},
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = paddingValues.calculateTopPadding())
    ) {
        val coroutineScope = rememberCoroutineScope()

        TextTabRow(
            modifier = Modifier.padding(horizontal = 32.dp),
            coroutineScope = coroutineScope,
            tabs = pages.map { stringResource(it.titleResId) },
            height = AppDimensions.tabBarHeight,
            pagerState = pagerState,
            backgroundColor = Color.Transparent
        )
        Box(modifier = Modifier.weight(1F)) {
            HorizontalPager(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = paddingValues.calculateBottomPadding()),
                userScrollEnabled = false,
                state = pagerState
            ) { page ->
                when (pages[page]) {
                    HomePages.MOVIES -> MoviesScreen(
                        onMediaClick = onMediaClick,
                        onGenreClick = onGenreClick,
                        onViewAll = onViewAll,
                    )

                    HomePages.TV_SHOW -> TvShowScreen(
                        onMediaClick = onMediaClick,
                        onGenreClick = onGenreClick,
                        onViewAll = onViewAll,
                    )
                }
            }
        }
    }
}