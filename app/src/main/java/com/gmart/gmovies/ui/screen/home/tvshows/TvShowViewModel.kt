package com.gmart.gmovies.ui.screen.home.tvshows

import androidx.lifecycle.viewModelScope
import com.gmart.data.source.utils.IoDispatcher
import com.gmart.domain.model.Resource
import com.gmart.domain.usecase.GenreMoviesUseCase
import com.gmart.domain.usecase.TvShowsUseCase
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
class TvShowViewModel @Inject constructor(
    private val tvShowsUseCase: TvShowsUseCase,
    private val genreUseCase: GenreMoviesUseCase,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : BaseViewModel<HomeViewState, HomeEvent, HomeEffect>(dispatcher) {

    init {
        getTvShows()
    }

    override fun setInitialState() = HomeViewState()

    override fun handleEvents(event: HomeEvent) {
        when (event) {
            HomeEvent.GetLists -> getTvShows()
            is HomeEvent.OnMediaClick ->
                setEffect(HomeEffect.NavigateToDetails(event.id, event.type))

            is HomeEvent.OnViewAll -> setEffect(HomeEffect.NavigateToExplorer(event.mediaListType))
            is HomeEvent.OnGenreClick ->
                setEffect(HomeEffect.NavigateToExplorerWithGenre(event.id, event.type))
        }
    }

    private fun getTvShows() {
        viewModelScope.launch(dispatcher) {

            val genresDeferred = async { genreUseCase.getTvGenres() }
            val popularDeferred = async { tvShowsUseCase.getPopularTvShows() }
            val airingTodayDeferred = async { tvShowsUseCase.getAiringToday() }
            val onAirDeferred = async { tvShowsUseCase.getOnAir() }

            combine(
                genresDeferred.await(),
                popularDeferred.await(),
                airingTodayDeferred.await(),
                onAirDeferred.await(),
                ::Quadruple
            ).onStart {
                setViewState(HomeViewState(isLoading = true))
            }.catch { throwable ->
                logE(throwable.message.toString())
                setViewState(HomeViewState(errorMessage = "Something went wrong"))
            }.collect { (genreResource, popularResource, airingTodayResource, onAirResource) ->
                val genres = when (genreResource) {
                    is Resource.Success -> genreResource.response
                    else -> null
                }
                val popular = when (popularResource) {
                    is Resource.Success -> popularResource.response
                    else -> null
                }
                val airingToday = when (airingTodayResource) {
                    is Resource.Success -> airingTodayResource.response
                    else -> null
                }
                val onAir = when (onAirResource) {
                    is Resource.Success -> onAirResource.response
                    else -> null
                }

                setViewState(
                    HomeViewState(
                        popular = popular,
                        nowPlaying = airingToday,
                        upComing = onAir,
                        genres = genres
                    )
                )
            }
        }
    }
}
