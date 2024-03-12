package com.gmart.gmovies.ui.screen.search

import com.gmart.domain.model.MediaType

sealed class SearchEvent {
    object CleanResults : SearchEvent()
    data class SearchMedia(val query: String, val mediaType: MediaType) : SearchEvent()
}