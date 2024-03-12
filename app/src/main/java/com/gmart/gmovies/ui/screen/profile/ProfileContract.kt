package com.gmart.gmovies.ui.screen.profile

import com.gmart.domain.model.Account

data class ProfileViewState(
    val data: Account? = null,
    val showMustBeSignIn: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)

sealed class ProfileEvent {
    object GetAccountDetails : ProfileEvent()
    object SignOut : ProfileEvent()
}