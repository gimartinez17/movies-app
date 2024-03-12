package com.gmart.data.model

import com.google.gson.annotations.SerializedName

data class RequestTokenRequest(
    @SerializedName("redirect_to")
    val redirectTo: String
)
