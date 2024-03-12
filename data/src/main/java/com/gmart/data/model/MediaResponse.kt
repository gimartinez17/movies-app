package com.gmart.data.model

import com.google.gson.annotations.SerializedName
import com.gmart.domain.model.MediaList

data class MediaResponse(
    var page: Int,
    @SerializedName("total_results")
    var totalResults: Int,
    @SerializedName("total_pages")
    var totalPages: Int,
    var results: List<MediaEntity>
): PagingResponse<MediaList> {
    override fun toModel(): MediaList = MediaList(
        page = page,
        totalResults = totalResults,
        totalPages = totalPages,
        results = results.map { it.mapToModel() }
    )
}
