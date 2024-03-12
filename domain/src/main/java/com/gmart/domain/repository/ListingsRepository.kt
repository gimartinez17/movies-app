package com.gmart.domain.repository

import com.gmart.domain.model.Detail
import com.gmart.domain.model.MediaType
import com.gmart.domain.model.Resource
import com.gmart.domain.model.UserList
import kotlinx.coroutines.flow.Flow

interface ListingsRepository {
    suspend fun getFavouriteMovies(accountId: String): Flow<Resource<List<Detail>>>
    suspend fun getFavouriteTvShows(accountId: String): Flow<Resource<List<Detail>>>
    suspend fun getMyLists(accountId: String): Flow<Resource<List<UserList>>>
    suspend fun getListDetails(id: Int): Flow<Resource<List<Detail>>>
    suspend fun removeItem(listId: Int, mediaId: Int, mediaType: MediaType): Flow<Resource<Boolean>>
    suspend fun postFavourite(id: Int, mediaType: MediaType, favourite: Boolean, account: String):
            Flow<Resource<Boolean>>

    suspend fun createList(
        name: String,
        description: String,
        public: Boolean,
        sortBy: String,
        language: String,
        country: String
    ): Flow<Resource<Boolean>>

    suspend fun editList(
        listId: Int,
        name: String,
        description: String,
        public: Boolean,
        sortBy: String,
        language: String,
        country: String
    ): Flow<Resource<Boolean>>

    suspend fun addItems(listId: Int, mediaId: Int, mediaType: MediaType): Flow<Resource<Boolean>>
    suspend fun deleteList(listId: Int): Flow<Resource<Boolean>>
}