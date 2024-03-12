package com.gmart.data.paging

import com.gmart.data.model.PagingResponse
import com.gmart.data.source.remote.ApiService
import com.gmart.data.source.utils.safeApiCall
import com.gmart.domain.model.MediaList
import com.gmart.domain.model.Resource

class PopularTvPagingSource(
    private val api: ApiService,
    totalPages: Int? = null,
) : DetailsPagingSource(totalPages = totalPages) {

    override suspend fun fetchData(page: Int): Resource<PagingResponse<MediaList>> {
        return safeApiCall { api.getPopularTv(page) }
    }
}
