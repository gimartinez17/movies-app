package com.gmart.data.model

import com.google.gson.annotations.SerializedName
import com.gmart.domain.model.MediaList

data class TvShowsResponse(
    var page: Int,
    @SerializedName("total_results")
    var totalResults: Int,
    @SerializedName("total_pages")
    var totalPages: Int,
    var results: List<TvShowEntity>
): PagingResponse<MediaList> {
    override fun toModel(): MediaList = MediaList(
        page = page,
        totalResults = totalResults,
        totalPages = totalPages,
        results = results.map { it.mapToModel() }
    )
}