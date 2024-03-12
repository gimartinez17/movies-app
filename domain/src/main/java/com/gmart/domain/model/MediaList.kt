package com.gmart.domain.model

enum class MediaListType(var title: String? = null) {
    TRENDING_ALL("Trending"),
    POPULAR_MOVIES("Popular"),
    POPULAR_TV("Popular"),
    UPCOMING("Upcoming"),
    NOW_PLAYING("Now Playing"),
    ON_AIR("On Air"),
    AIRING_TODAY("Air Today"),
    MOVIE_GENRES("Genre"),
    TV_GENRES("Genre");

    companion object {
        fun fromString(type: String?): MediaListType? = MediaListType.values()
            .find { it.name == type }

        fun MediaListType.mediaType(): MediaType? {
            return when (this) {
                POPULAR_MOVIES, UPCOMING, NOW_PLAYING, MOVIE_GENRES -> MediaType.MOVIE
                POPULAR_TV, ON_AIR, AIRING_TODAY, TV_GENRES -> MediaType.TV
                else -> null
            }
        }
    }
}

data class MediaList(
    var page: Int,
    var totalResults: Int,
    var totalPages: Int,
    var results: List<Detail>
)
