package com.gmart.data.paging

import com.gmart.data.model.PagingResponse
import com.gmart.data.source.remote.ApiService
import com.gmart.data.source.utils.safeApiCall
import com.gmart.domain.model.MediaList
import com.gmart.domain.model.Resource
import java.time.LocalDate

class OnAirPagingSource(
    private val api: ApiService,
    totalPages: Int? = null,
) : DetailsPagingSource(totalPages = totalPages) {

    override suspend fun fetchData(page: Int): Resource<PagingResponse<MediaList>> {
        val gte = LocalDate.now().plusDays(1).toString()
        val lte = LocalDate.now().plusDays(8).toString()
        return safeApiCall { api.getOnTheAir(page, gte, lte) }
    }
}
