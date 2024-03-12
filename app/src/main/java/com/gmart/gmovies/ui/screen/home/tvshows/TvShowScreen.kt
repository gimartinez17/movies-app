package com.gmart.gmovies.ui.screen.home.tvshows

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gmart.domain.model.MediaListType
import com.gmart.domain.model.MediaType
import com.gmart.domain.model.MediaType.TV
import com.gmart.gmovies.ui.composable.AppError
import com.gmart.gmovies.ui.composable.Loading
import com.gmart.gmovies.ui.screen.home.HomeEffect
import com.gmart.gmovies.ui.screen.home.HomeEvent
import com.gmart.gmovies.ui.screen.home.composable.MediaContent

@Composable
fun TvShowScreen(
    onMediaClick: (Int, MediaType) -> Unit = { _, _ -> },
    onViewAll: (MediaListType) -> Unit = {},
    onGenreClick: (Int, MediaListType) -> Unit = { _, _ -> },
    viewModel: TvShowViewModel = hiltViewModel(),
) {
    val tvShowViewState by viewModel.viewState.collectAsStateWithLifecycle()
    val tvShowEffect by viewModel.effect.collectAsStateWithLifecycle(HomeEffect.None)

    if (tvShowViewState.isLoading) Loading(Modifier.fillMaxSize())

    if (tvShowViewState.popular != null || tvShowViewState.nowPlaying != null || tvShowViewState.upComing != null) {
        val popular = tvShowViewState.popular
        val nowPlaying = tvShowViewState.nowPlaying
        val upComing = tvShowViewState.upComing
        MediaContent(
            modifier = Modifier.fillMaxSize(),
            mediaType = TV,
            popular = popular,
            nowPlaying = nowPlaying,
            upComing = upComing,
            genres = tvShowViewState.genres,
            onMediaClick = { viewModel.setEvent(HomeEvent.OnMediaClick(id = it, type = TV)) },
            onViewAll = { viewModel.setEvent(HomeEvent.OnViewAll(mediaListType = it)) },
            onGenreClick = { id, title ->
                MediaListType.TV_GENRES.title = title
                viewModel.setEvent(
                    HomeEvent.OnGenreClick(id = id, type = MediaListType.TV_GENRES)
                )
            },
        )

    }

    LaunchedEffect(tvShowEffect) {
        when (val effect = tvShowEffect) {
            HomeEffect.None -> {}
            is HomeEffect.NavigateToDetails -> onMediaClick(effect.id, effect.type)
            is HomeEffect.NavigateToExplorer -> onViewAll(effect.mediaListType)
            is HomeEffect.NavigateToExplorerWithGenre -> onGenreClick(effect.genreId, effect.type)
        }
    }

    if (tvShowViewState.errorMessage != null)
        AppError(onRetry = { viewModel.handleEvents(HomeEvent.GetLists) })
}
