package com.gmart.data.repository

import com.gmart.data.model.CreateListRequest
import com.gmart.data.model.FavouriteRequest
import com.gmart.data.model.Item
import com.gmart.data.model.ListItemRequest
import com.gmart.data.model.mapToModel
import com.gmart.data.source.remote.ApiService
import com.gmart.data.source.utils.safeApiCall
import com.gmart.domain.model.MediaType
import com.gmart.domain.repository.ListingsRepository
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ListingRepositoryImpl @Inject constructor(private val api: ApiService) : ListingsRepository {

    override suspend fun getFavouriteMovies(accountId: String) = flow {
        val response = safeApiCall { api.getFavouriteMovies(accountId) }
            .mapResult { response -> response.results.map { it.mapToModel() } }
        emit(response)
    }

    override suspend fun getFavouriteTvShows(accountId: String) = flow {
        val response = safeApiCall { api.getFavouriteTvShows(accountId) }
            .mapResult { response -> response.results.map { it.mapToModel() } }
        emit(response)
    }

    override suspend fun getMyLists(accountId: String) = flow {
        val response = safeApiCall { api.getMyLists(accountId) }
            .mapResult { response -> response.results.map { it.mapToModel() } }
        emit(response)
    }

    override suspend fun getListDetails(id: Int) = flow {
        val response = safeApiCall { api.getListDetails(id) }
            .mapResult { response -> response.results.map { it.mapToModel() } }
        emit(response)
    }

    override suspend fun postFavourite(
        id: Int,
        mediaType: MediaType,
        favourite: Boolean,
        account: String
    ) = flow {
        val body = FavouriteRequest(id, mediaType.type, favourite)
        val response = safeApiCall { api.postFavourite(account, body) }
            .mapResult { response -> response.success }
        emit(response)
    }

    override suspend fun removeItem(listId: Int, mediaId: Int, mediaType: MediaType) = flow {
        val body = ListItemRequest(listOf(Item(mediaId = mediaId, mediaType = mediaType.type)))
        val response = safeApiCall { api.removeItem(listId, body) }
            .mapResult { response -> response.success }
        emit(response)
    }

    override suspend fun createList(
        name: String,
        description: String,
        public: Boolean,
        sortBy: String,
        language: String,
        country: String
    ) = flow {
        val isPublic = if (public) 1 else 0
        val request = CreateListRequest(name, description, isPublic, sortBy, language, country)
        val response = safeApiCall { api.createList(request) }
            .mapResult { response -> response.success }
        emit(response)
    }

    override suspend fun editList(
        listId: Int,
        name: String,
        description: String,
        public: Boolean,
        sortBy: String,
        language: String,
        country: String
    ) = flow {
        val isPublic = if (public) 1 else 0
        val request = CreateListRequest(name, description, isPublic, sortBy, language, country)
        val response = safeApiCall { api.editList(listId, request) }
            .mapResult { response -> response.success }
        emit(response)
    }

    override suspend fun addItems(listId: Int, mediaId: Int, mediaType: MediaType) = flow {
        val body = ListItemRequest(listOf(Item(mediaId = mediaId, mediaType = mediaType.type)))
        val response = safeApiCall { api.addItems(listId, body) }
            .mapResult { response -> response.success }
        emit(response)
    }

    override suspend fun deleteList(listId: Int) = flow {
        val response = safeApiCall { api.deleteList(listId) }
            .mapResult { response -> response.success }
        emit(response)
    }
}