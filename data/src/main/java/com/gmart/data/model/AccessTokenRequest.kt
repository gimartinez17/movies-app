package com.gmart.data.model

import com.google.gson.annotations.SerializedName

data class AccessTokenRequest(
    @SerializedName("request_token")
    val requestToken: String
)
