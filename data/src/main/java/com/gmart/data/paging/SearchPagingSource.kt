package com.gmart.data.paging

import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.gmart.data.model.mapToModel
import com.gmart.data.source.remote.ApiService
import com.gmart.data.source.utils.safeApiCall
import com.gmart.domain.model.Detail
import com.gmart.domain.model.MediaType
import com.gmart.domain.model.Resource

class SearchPagingSource(
    private val api: ApiService,
    private val mediaType: MediaType,
    private val query: String,
) : PagingSource<Int, Detail>() {

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Detail> {

        val currentPage = params.key ?: 1
        val result = when (mediaType) {
            MediaType.MOVIE -> safeApiCall { api.searchMovies(query, currentPage) }
            MediaType.TV -> safeApiCall { api.searchTvShows(query, currentPage) }
        }

        return when (result) {
            is Resource.Success -> {
                val nextKey =
                    if (result.response.totalPages == currentPage) null else currentPage + 1
                LoadResult.Page(
                    data = result.response.results.map { it.mapToModel() },
                    nextKey = nextKey,
                    prevKey = null
                )
            }

            is Resource.Failure -> LoadResult.Error(result.error ?: Throwable())
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Detail>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}
