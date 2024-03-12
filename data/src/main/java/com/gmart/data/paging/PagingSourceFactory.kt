package com.gmart.data.paging

import androidx.paging.PagingSource
import com.gmart.data.source.remote.ApiService
import com.gmart.domain.model.Detail
import com.gmart.domain.model.MediaListType
import javax.inject.Inject

interface PagingSourceFactory {
    fun create(mediaType: MediaListType, id: Int?, totalPages: Int?):
            PagingSource<Int, Detail>
}

class PagingSourceFactoryImpl @Inject constructor(private val api: ApiService) :
    PagingSourceFactory {

    override fun create(mediaType: MediaListType, id: Int?, totalPages: Int?):
            PagingSource<Int, Detail> = when (mediaType) {
        MediaListType.TRENDING_ALL -> TrendingPagingSource(api, totalPages)
        MediaListType.POPULAR_MOVIES -> PopularMoviesPagingSource(api, totalPages)
        MediaListType.POPULAR_TV -> PopularTvPagingSource(api, totalPages)
        MediaListType.UPCOMING -> UpcomingPagingSource(api, totalPages)
        MediaListType.NOW_PLAYING -> NowPlayingPagingSource(api, totalPages)
        MediaListType.ON_AIR -> OnAirPagingSource(api, totalPages)
        MediaListType.AIRING_TODAY -> AiringTodayPagingSource(api, totalPages)
        MediaListType.MOVIE_GENRES -> MoviesByGenresPagingSource(api, id ?: 0, totalPages)
        MediaListType.TV_GENRES -> TvShowsByGenresPagingSource(api, id ?: 0, totalPages)
    }
}
