package com.gmart.domain.model

class Genre(
    var id: Int,
    var name: String,
    var image: String? = null
)

class GenreList(
    var data: List<Genre> = emptyList()
)