package com.gmart.domain.usecase

import com.gmart.domain.model.MediaType
import com.gmart.domain.repository.ListingsRepository
import javax.inject.Inject

class ListingsUseCase @Inject constructor(private val repository: ListingsRepository) {

    suspend fun getMyLists(accountId: String) = repository.getMyLists(accountId)
    suspend fun getListDetails(accountId: String, id: Int) = when (id) {
        0 -> repository.getFavouriteMovies(accountId)
        1 -> repository.getFavouriteTvShows(accountId)
        else -> repository.getListDetails(id)
    }

    suspend fun postFavourite(id: Int, mediaType: MediaType, favourite: Boolean, account: String) =
        repository.postFavourite(id, mediaType, favourite, account)

    suspend fun removeItem(listId: Int, mediaId: Int, mediaType: MediaType) =
        repository.removeItem(listId, mediaId, mediaType)

    suspend fun createList(
        name: String,
        description: String,
        isPublic: Boolean,
        sortBy: String,
        language: String,
        country: String
    ) = repository.createList(name, description, isPublic, sortBy, language, country)

    suspend fun editList(
        listId: Int,
        name: String,
        description: String,
        isPublic: Boolean,
        sortBy: String,
        language: String,
        country: String
    ) = repository.editList(listId, name, description, isPublic, sortBy, language, country)

    suspend fun deleteList(listId: Int) = repository.deleteList(listId)

    suspend fun addItems(listId: Int, mediaId: Int, mediaType: MediaType) =
        repository.addItems(listId, mediaId, mediaType)
}