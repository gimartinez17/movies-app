package com.gmart.gmovies.ui.screen.person

import com.gmart.domain.model.MediaType
import com.gmart.domain.model.Person

data class PersonViewState(
    val details: Person? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)

sealed class PersonEvent {
    data class GetDetails(val id: Int) : PersonEvent()
    data class OnMediaClick(val id: Int, val mediaType: MediaType) : PersonEvent()
}

sealed class PersonEffect {
    object None : PersonEffect()
    data class NavigateToDetails(val id: Int, val mediaType: MediaType) : PersonEffect()
}