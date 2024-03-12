package com.gmart.data.model

import com.google.gson.annotations.SerializedName
import com.gmart.domain.model.Crew


data class CrewEntity(
    @SerializedName("credit_id")
    var creditId: String,
    var department: String,
    var gender: Int,
    var id: Int,
    var job: String,
    var name: String,
    @SerializedName("profile_path")
    var profilePath: String?
)

fun CrewEntity.mapToModel() = Crew(
    creditId = creditId,
    department = department,
    gender = gender,
    id = id,
    job = job,
    name = name,
    profilePath = profilePath
)