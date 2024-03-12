package com.gmart.domain.repository

import androidx.paging.PagingData
import com.gmart.domain.model.Detail
import com.gmart.domain.model.MediaListType
import kotlinx.coroutines.flow.Flow

interface ExplorerRepository {

    suspend fun getMediaList(type: MediaListType, id: Int?, totalPages: Int?):
            Flow<PagingData<Detail>>
}