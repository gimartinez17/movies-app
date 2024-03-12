package com.gmart.data.model

import com.google.gson.annotations.SerializedName

data class AvatarEntity(
    val gravatar: GravatarEntity? = null,
    @SerializedName("tmdb")
    val avatarPath: AvatarPathEntity? = null,
)

data class GravatarEntity(val hash: String? = null)

data class AvatarPathEntity(
    @SerializedName("avatar_path")
    val avatarPath: String? = null
)