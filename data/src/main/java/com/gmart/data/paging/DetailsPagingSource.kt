package com.gmart.data.paging

import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.gmart.data.model.PagingResponse
import com.gmart.domain.model.Detail
import com.gmart.domain.model.MediaList
import com.gmart.domain.model.Resource

abstract class DetailsPagingSource(private val totalPages: Int?) : PagingSource<Int, Detail>() {

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Detail> {
        val currentPage = params.key ?: 1

        return when (val result = fetchData(currentPage)) {
            is Resource.Success -> {
                val response: MediaList = result.response.toModel()
                val lastPage = totalPages ?: response.totalPages
                val nextKey = if (lastPage == currentPage) null else currentPage + 1
                LoadResult.Page(
                    data = response.results.distinctBy { it.id },
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

    abstract suspend fun fetchData(page: Int): Resource<PagingResponse<MediaList>>
}