package com.gmart.domain.model

enum class MediaType(val type: String) {
    MOVIE("movie"),
    TV("tv");

    companion object {
        fun fromString(type: String?): MediaType? = values().find { it.type == type }
    }
}

data class Detail(
    var id: Int,
    var adult: Boolean = false,
    var backdropPath: String? = null,
    var createdBy: List<String>? = null,
    var genres: List<Genre>? = null,
    var genreIds: List<Int>? = null,
    var homepage: String? = null,
    var overview: String? = null,
    var popularity: Float = 0F,
    var posterPath: String? = null,
    var releaseDate: String? = null,
    var runtime: Int? = null,
    var mediaType: MediaType? = null,
    var status: String? = null,
    var originCountry: List<String>? = null,
    var title: String,
    var originalTitle: String? = null,
    var voteAverage: Float = 0F,
    var voteCount: Int = 0,
    var numberOfSeasons: Int? = null,
    var credits: Credit? = null,
    var recommendations: List<Detail>? = null,
    var watchLocale: WatchLocale? = null,
    val videos: List<Video>? = null,
)
