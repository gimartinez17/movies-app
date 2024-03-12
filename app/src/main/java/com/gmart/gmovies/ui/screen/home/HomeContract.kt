package com.gmart.gmovies.ui.screen.home

import androidx.compose.runtime.Immutable
import com.gmart.domain.model.Detail
import com.gmart.domain.model.Genre
import com.gmart.domain.model.MediaListType
import com.gmart.domain.model.MediaType

@Immutable
data class HomeViewState(
    val genres: List<Genre>? = null,
    val popular: List<Detail>? = null,
    val nowPlaying: List<Detail>? = null,
    val upComing: List<Detail>? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)

@Immutable
sealed class HomeEvent {
    object GetLists : HomeEvent()
    data class OnMediaClick(val id: Int, val type: MediaType) : HomeEvent()
    data class OnGenreClick(val id: Int, val type: MediaListType) : HomeEvent()
    data class OnViewAll(val mediaListType: MediaListType) : HomeEvent()
}

@Immutable
sealed class HomeEffect {
    object None : HomeEffect()
    data class NavigateToDetails(val id: Int, val type: MediaType) : HomeEffect()
    data class NavigateToExplorer(val mediaListType: MediaListType) : HomeEffect()
    data class NavigateToExplorerWithGenre(val genreId: Int, val type: MediaListType) : HomeEffect()
}

class HomeEventHandler(
    val onMediaClick: (Int) -> Unit = {},
    val onGenreClick: (Int, String) -> Unit = { _, _ -> },
    val onViewAll: (MediaListType) -> Unit = {},
)