package com.gmart.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.gmart.data.source.remote.ApiService
import com.gmart.data.paging.SearchPagingSource
import com.gmart.domain.model.MediaType
import com.gmart.domain.repository.SearchRepository
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(private val api: ApiService) : SearchRepository {

    override suspend fun searchMultiMedia(query: String, mediaType: MediaType, page: Int) = Pager(
        config = PagingConfig(pageSize = 20),
        pagingSourceFactory = { SearchPagingSource(api, mediaType, query) }
    ).flow
}