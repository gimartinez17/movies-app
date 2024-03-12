package com.gmart.gmovies.ui.screen.home.movies

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gmart.domain.model.MediaListType
import com.gmart.domain.model.MediaType
import com.gmart.domain.model.MediaType.MOVIE
import com.gmart.gmovies.ui.composable.AppError
import com.gmart.gmovies.ui.composable.Loading
import com.gmart.gmovies.ui.screen.home.HomeEffect
import com.gmart.gmovies.ui.screen.home.HomeEvent
import com.gmart.gmovies.ui.screen.home.composable.MediaContent

@Composable
fun MoviesScreen(
    onMediaClick: (Int, MediaType) -> Unit = { _, _ -> },
    onViewAll: (MediaListType) -> Unit = {},
    onGenreClick: (Int, MediaListType) -> Unit = { _, _ -> },
    viewModel: MoviesViewModel = hiltViewModel(),
) {
    val movieViewState by viewModel.viewState.collectAsStateWithLifecycle()
    val movieEffect by viewModel.effect.collectAsStateWithLifecycle(HomeEffect.None)

    if (movieViewState.isLoading) Loading(Modifier.fillMaxSize())

    if (movieViewState.popular != null || movieViewState.nowPlaying != null || movieViewState.upComing != null) {
        MediaContent(
            modifier = Modifier.fillMaxSize(),
            mediaType = MOVIE,
            popular = movieViewState.popular,
            nowPlaying = movieViewState.nowPlaying,
            upComing = movieViewState.upComing,
            genres = movieViewState.genres,
            onMediaClick = { viewModel.setEvent(HomeEvent.OnMediaClick(id = it, type = MOVIE)) },
            onViewAll = { viewModel.setEvent(HomeEvent.OnViewAll(mediaListType = it)) },
            onGenreClick = { id, title ->
                MediaListType.MOVIE_GENRES.title = title
                viewModel.setEvent(
                    HomeEvent.OnGenreClick(id = id, type = MediaListType.MOVIE_GENRES)
                )
            },
        )
    }

    if (movieViewState.errorMessage != null)
        AppError(onRetry = { viewModel.handleEvents(HomeEvent.GetLists) })

    LaunchedEffect(movieEffect) {
        when (val effect = movieEffect) {
            HomeEffect.None -> {}
            is HomeEffect.NavigateToDetails -> onMediaClick(effect.id, effect.type)
            is HomeEffect.NavigateToExplorer -> onViewAll(effect.mediaListType)
            is HomeEffect.NavigateToExplorerWithGenre -> onGenreClick(effect.genreId, effect.type)
        }
    }
}