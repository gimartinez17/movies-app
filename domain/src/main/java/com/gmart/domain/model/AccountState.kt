package com.gmart.domain.model

data class AccountState(
    val id: Int,
    val favorite: Boolean = false,
    val rated: Boolean = false,
    val watchlist: Boolean = false
)
