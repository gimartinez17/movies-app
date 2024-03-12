package com.gmart.gmovies.ui.screen.details

import androidx.compose.runtime.Immutable
import com.gmart.domain.model.AccountState
import com.gmart.domain.model.Detail
import com.gmart.domain.model.MediaType
import com.gmart.domain.model.UserList
import com.gmart.domain.model.WatchProviders

@Immutable
data class DetailsViewState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val details: Detail? = null,
    val accountState: AccountState? = null,
    val watchProviders: WatchProviders? = null,
    val userList: List<UserList>? = null,
    val snackBarMessage: String? = null,
)

@Immutable
sealed class DetailsEvent {
    object GetDetails : DetailsEvent()
    data class OnFavouriteClick(val isFavourite: Boolean) : DetailsEvent()
    data class OnMediaClick(val id: Int, val mediaType: MediaType) : DetailsEvent()
    data class OnCastItemClick(val id: Int) : DetailsEvent()
    object OnViewAllCastClick : DetailsEvent()
    data class OnVideoClick(val videoKey: String) : DetailsEvent()
    data class OnAddToListClick(val listId: Int) : DetailsEvent()
}

@Immutable
sealed class DetailsEffect {
    object None : DetailsEffect()
    data class NavigateToDetails(val id: Int, val mediaType: MediaType) : DetailsEffect()
    data class NavigateToCast(val id: Int) : DetailsEffect()
    data class NavigateToAllCast(val id: Int, val mediaType: MediaType) : DetailsEffect()
    data class NavigateToVideoPlayerScreen(val videoKey: String) : DetailsEffect()
}
