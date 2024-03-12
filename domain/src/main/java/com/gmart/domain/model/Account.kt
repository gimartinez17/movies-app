package com.gmart.domain.model

data class Account(
    val id: Int? = null,
    val name: String? = null,
    val username: String? = null,
    val avatarPath: String? = null,
    val language: String? = null,
    val country: String? = null,
    val includeAdult: Boolean? = null,
)
