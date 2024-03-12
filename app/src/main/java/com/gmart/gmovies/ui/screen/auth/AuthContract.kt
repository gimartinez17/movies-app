package com.gmart.gmovies.ui.screen.auth

data class AuthViewState(
    val token: String? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)

sealed class AuthEffect {
    object None : AuthEffect()
    object NavigateToPreviousScreen : AuthEffect()
}

sealed class AuthEvent {
    object GetRequestToken : AuthEvent()
    data class GetAccessToken(val requestToken: String) : AuthEvent()
}