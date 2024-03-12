package com.gmart.data.model

import com.google.gson.annotations.SerializedName

data class CreateListRequest(
    val name: String,
    val description: String,
    val public: Int,
    @SerializedName("sort_by")
    val sortBy: String,
    @SerializedName("iso_639_1")
    val languageCode: String,
    @SerializedName("iso_3166_1")
    val countryCode: String? = null,
)
