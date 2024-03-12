package com.gmart.domain.model

data class UserListResults(
    var page: Int,
    var totalResults: Int,
    var totalPages: Int,
    var results: List<UserList>
)
