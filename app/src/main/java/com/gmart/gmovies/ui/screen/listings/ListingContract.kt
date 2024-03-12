package com.gmart.gmovies.ui.screen.listings

import com.gmart.domain.model.Detail
import com.gmart.domain.model.ListType
import com.gmart.domain.model.MediaType

data class ListingViewState(
    val data: MutableList<Detail>? = null,
    val listType: ListType? = null,
    val showMustBeSignedIn: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val snackBarMessage: String? = null,
)

sealed class ListingEvent {
    object GetListings : ListingEvent()
    data class SelectList(val idx: Int) : ListingEvent()
    data class GetListDetails(val id: Int) : ListingEvent()
    data class RemoveMedia(val listId: Int, val mediaId: Int, val mediaType: MediaType) :
        ListingEvent()

    data class DeleteList(val listId: Int) : ListingEvent()
    data class CreateList(
        val name: String,
        val description: String,
        val isPublic: Boolean,
        val sortBy: String
    ) : ListingEvent()

    data class EditList(
        val listId: Int,
        val name: String,
        val description: String,
        val isPublic: Boolean,
        val sortBy: String
    ) : ListingEvent()
}