package com.gmart.data.model

import com.gmart.domain.model.AccountState

data class AccountStateResponse(
    val id: Int,
    val favorite: Boolean,
    val rated: Boolean,
    val watchlist: Boolean
)

fun AccountStateResponse.mapToModel() = AccountState(
    id = id,
    favorite = favorite,
    rated = rated,
    watchlist = watchlist
)
