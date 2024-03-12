package com.gmart.data.model

import com.google.gson.annotations.SerializedName

data class ListDetailsResponse(
    val description: String,
    val id: Int,
    @SerializedName("item_count")
    val itemCount: Int,
    val results: List<MediaEntity>
)
