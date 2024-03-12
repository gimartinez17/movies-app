package com.gmart.data.model

import com.google.gson.annotations.SerializedName

data class SignOutRequest(
    @SerializedName("access_token")
    val accessToken: String
)
