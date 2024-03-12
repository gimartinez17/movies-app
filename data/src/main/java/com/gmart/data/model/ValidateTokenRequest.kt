package com.gmart.data.model

import com.google.gson.annotations.SerializedName

data class ValidateTokenRequest(
    val username: String,
    val password: String,
    @SerializedName("request_token")
    val requestToken: String
)
