package com.gmart.domain.repository

import androidx.paging.PagingData
import com.gmart.domain.model.Detail
import com.gmart.domain.model.MediaType
import kotlinx.coroutines.flow.Flow

interface SearchRepository {
    suspend fun searchMultiMedia(query: String, mediaType: MediaType, page: Int):
            Flow<PagingData<Detail>>
}