package com.gmart.gmovies.ui.screen.home.movies

import androidx.lifecycle.viewModelScope
import com.gmart.data.source.utils.IoDispatcher
import com.gmart.domain.model.Resource
import com.gmart.domain.usecase.GenreMoviesUseCase
import com.gmart.domain.usecase.MoviesUseCase
import com.gmart.gmovies.ui.base.BaseViewModel
import com.gmart.gmovies.ui.screen.home.HomeEffect
import com.gmart.gmovies.ui.screen.home.HomeEvent
import com.gmart.gmovies.ui.screen.home.HomeViewState
import com.gmart.gmovies.utils.Quadruple
import com.gmart.gmovies.utils.logE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel @Inject constructor(
    private val moviesUseCase: MoviesUseCase,
    private val genreUseCase: GenreMoviesUseCase,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : BaseViewModel<HomeViewState, HomeEvent, HomeEffect>(dispatcher) {

    init {
        getMovies()
    }

    override fun setInitialState() = HomeViewState()

    override fun handleEvents(event: HomeEvent) {
        when (event) {
            HomeEvent.GetLists -> getMovies()
            is HomeEvent.OnMediaClick ->
                setEffect(HomeEffect.NavigateToDetails(event.id, event.type))

            is HomeEvent.OnViewAll -> setEffect(HomeEffect.NavigateToExplorer(event.mediaListType))
            is HomeEvent.OnGenreClick ->
                setEffect(HomeEffect.NavigateToExplorerWithGenre(event.id, event.type))
        }
    }

    private fun getMovies() {
        viewModelScope.launch(dispatcher) {
            val genresDeferred = async { genreUseCase.getMovieGenres() }
            val popularDeferred = async { moviesUseCase.getPopularMovies() }
            val nowPlayingDeferred = async { moviesUseCase.getNowPlayingMovies() }
            val upcomingDeferred = async { moviesUseCase.getUpcomingMovies() }

            combine(
                genresDeferred.await(),
                popularDeferred.await(),
                nowPlayingDeferred.await(),
                upcomingDeferred.await(),
                ::Quadruple
            ).onStart {
                setViewState(HomeViewState(isLoading = true))
            }.catch { throwable ->
                logE(throwable.message.toString())
                setViewState(HomeViewState(errorMessage = "Something went wrong"))
            }.collect { (genreResource, popularResource, nowPlayingResource, upcomingResource) ->
                val genres = when (genreResource) {
                    is Resource.Success -> genreResource.response
                    else -> null
                }
                val popular = when (popularResource) {
                    is Resource.Success -> popularResource.response
                    else -> null
                }
                val nowPlaying = when (nowPlayingResource) {
                    is Resource.Success -> nowPlayingResource.response
                    else -> null
                }
                val upcoming = when (upcomingResource) {
                    is Resource.Success -> upcomingResource.response
                    else -> null
                }

                setViewState(
                    HomeViewState(
                        popular = popular,
                        nowPlaying = nowPlaying,
                        upComing = upcoming,
                        genres = genres
                    )
                )

                if (genres == null || popular == null || nowPlaying == null || upcoming == null) {
                    setViewState(HomeViewState(errorMessage = "Something went wrong"))
                }
            }
        }
    }
}
