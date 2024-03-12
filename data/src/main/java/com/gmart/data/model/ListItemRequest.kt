package com.gmart.data.model

import com.google.gson.annotations.SerializedName

data class ListItemRequest(val items: List<Item>)

data class Item(
    @SerializedName("media_id")
    val mediaId: Int,
    @SerializedName("media_type")
    val mediaType: String,
)
