package com.gmart.gmovies.ui.screen.explorer

import com.gmart.domain.model.MediaListType
import com.gmart.domain.model.MediaType

sealed class ExplorerEvent {
    data class GetMediaList(val mediaListType: MediaListType, val id: Int?) : ExplorerEvent()
    data class OnMediaClick(val id: Int, val mediaType: MediaType?) : ExplorerEvent()
}

sealed class ExplorerEffect {
    object None : ExplorerEffect()
    data class NavigateToDetails(val id: Int, val mediaType: MediaType) : ExplorerEffect()
}