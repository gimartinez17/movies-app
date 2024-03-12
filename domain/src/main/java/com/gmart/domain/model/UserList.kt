package com.gmart.domain.model

enum class ListType(var id: Int? = null) {
    FAVOURITE_MOVIES(0),
    FAVOURITE_TV(1),
    USER;

    companion object {
        fun ListType.mediaType(): MediaType? {
            return when (this) {
                FAVOURITE_MOVIES -> MediaType.MOVIE
                FAVOURITE_TV -> MediaType.TV
                else -> null
            }
        }
    }
}

data class UserList(
    val description: String? = null,
    val favoriteCount: Int? = null,
    val id: Int = 0,
    val itemCount: Int? = null,
    val language: String? = null,
    val listType: ListType,
    val name: String,
    val public: Boolean? = null,
    val sortBy: Int? = null,
    val posterPath: String? = null,
    val createdAt: String? = null
)