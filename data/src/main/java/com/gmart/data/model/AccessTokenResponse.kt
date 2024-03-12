package com.gmart.data.model

import com.google.gson.annotations.SerializedName
import com.gmart.domain.model.Authorization

data class AccessTokenResponse(
    val success: Boolean?,
    @SerializedName("access_token")
    val accessToken: String?,
    @SerializedName("account_id")
    val accountId: String?
)

fun AccessTokenResponse.mapToModel() = Authorization(
    success = success,
    accessToken = accessToken,
    accountId = accountId
)
