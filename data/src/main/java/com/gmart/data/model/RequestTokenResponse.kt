package com.gmart.data.model

import com.google.gson.annotations.SerializedName

data class RequestTokenResponse(
    @SerializedName("success")
    val success: Boolean?,
    @SerializedName("request_token")
    val requestToken: String?,
)
