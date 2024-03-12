package com.gmart.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.gmart.data.paging.PagingSourceFactory
import com.gmart.domain.model.MediaListType
import com.gmart.domain.repository.ExplorerRepository
import javax.inject.Inject

class ExplorerRepositoryImpl @Inject constructor(
    private val pagingSourceFactory: PagingSourceFactory,
) : ExplorerRepository {

    override suspend fun getMediaList(type: MediaListType, id: Int?, totalPages: Int?) =
        Pager(config = PagingConfig(pageSize = 20)) {
            pagingSourceFactory.create(type, id, totalPages)
        }.flow
}