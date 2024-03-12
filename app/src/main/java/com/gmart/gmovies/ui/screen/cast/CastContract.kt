package com.gmart.gmovies.ui.screen.cast

import com.gmart.domain.model.Cast

data class CastViewState(
    val data: List<Cast>? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)

sealed class CastEvent {
    object GetCast : CastEvent()
    data class OnPersonClick(val id: Int) : CastEvent()
}

sealed class CastEffect {
    object None : CastEffect()
    data class NavigateToPerson(val id: Int) : CastEffect()
}