package com.gmart.data.model

import com.google.gson.annotations.SerializedName
import com.gmart.domain.model.Account

data class AccountResponse(
    val avatar: AvatarEntity? = null,
    val id: Int? = null,
    val name: String? = null,
    val username: String? = null,
    @SerializedName("iso_639_1")
    val iso6391: String? = null,
    @SerializedName("iso_3166_1")
    val iso31661: String? = null,
    @SerializedName("include_adult")
    val includeAdult: Boolean? = null,
)

fun AccountResponse.mapToModel() = Account(
    id = id,
    name = name,
    username = username,
    avatarPath = avatar?.avatarPath?.avatarPath,
    language = iso6391,
    country = iso31661,
    includeAdult = includeAdult
)
