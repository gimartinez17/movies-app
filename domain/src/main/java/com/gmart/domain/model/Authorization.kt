package com.gmart.domain.model

data class Authorization(
    val success: Boolean?,
    val accessToken: String?,
    val accountId: String?
)
