package com.gmart.data.model

import com.google.gson.annotations.SerializedName
import com.gmart.domain.model.Cast

data class CastEntity(
    var id: Int,
    @SerializedName("cast_id")
    var castId: Int,
    var character: String,
    @SerializedName("credit_id")
    var creditId: String,
    var gender: Int,
    var name: String,
    var order: Int,
    @SerializedName("profile_path")
    var profilePath: String?
)

fun CastEntity.mapToModel() = Cast(
    id = id,
    castId = castId,
    character = character,
    creditId = creditId,
    gender = gender,
    name = name,
    order = order,
    profilePath = profilePath,
)
