package com.gmart.data.model

import com.google.gson.annotations.SerializedName
import com.gmart.domain.model.UserListResults

data class UserListsResponse(
    var page: Int,
    @SerializedName("total_results")
    var totalResults: Int,
    @SerializedName("total_pages")
    var totalPages: Int,
    var results: List<UserListEntity>
)

fun UserListsResponse.toModel(): UserListResults = UserListResults(
    page = page,
    totalResults = totalResults,
    totalPages = totalPages,
    results = results.map { it.mapToModel() }
)
