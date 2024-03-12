package com.gmart.data.model

import com.google.gson.annotations.SerializedName
import com.gmart.domain.model.Language

data class LanguageResponse(
    @SerializedName("iso_639_1")
    val language: String,
    @SerializedName("english_name")
    val englishName: String,
    @SerializedName("name")
    val nativeName: String
)

fun LanguageResponse.mapToModel() = Language(
    language = language,
    englishName = englishName,
    nativeName = nativeName
)
