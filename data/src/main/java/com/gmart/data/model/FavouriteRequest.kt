package com.gmart.data.model

import com.google.gson.annotations.SerializedName

data class FavouriteRequest(
    @SerializedName("media_id")
    val mediaId: Int,
    @SerializedName("media_type")
    val mediaType: String,
    val favorite: Boolean
)
