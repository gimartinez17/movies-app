package com.gmart.data.model

import com.google.gson.annotations.SerializedName
import com.gmart.domain.model.Country

data class CountryResponse(
    @SerializedName("iso_3166_1")
    val country: String,
    @SerializedName("english_name")
    val englishName: String,
    @SerializedName("native_name")
    val nativeName: String
)

fun CountryResponse.mapToModel() = Country(
    country = country,
    englishName = englishName,
    nativeName = nativeName
)
