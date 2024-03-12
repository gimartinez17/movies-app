package com.gmart.data.model

import com.google.gson.annotations.SerializedName
import com.gmart.domain.model.Detail
import com.gmart.domain.model.MediaType

data class MediaEntity(
    var id: Int,
    var adult: Boolean,
    @SerializedName("backdrop_path")
    var backdropPath: String?,
    @SerializedName("genre_ids")
    var genreIds: List<Int>?,
    var homepage: String?,
    var overview: String?,
    var popularity: Float,
    @SerializedName("poster_path")
    var posterPath: String?,
    @SerializedName("release_date")
    var releaseDate: String?,
    @SerializedName("first_air_date")
    var firstAirDate: String?,
    @SerializedName("media_type")
    var mediaType: String?,
    var title: String?,
    var name: String?,
    @SerializedName("original_title")
    var originalTitle: String?,
    @SerializedName("original_name")
    var originalName: String?,
    @SerializedName("origin_country")
    var originCountry: List<String>?,
    @SerializedName("vote_average")
    var voteAverage: Float,
    @SerializedName("vote_count")
    var voteCount: Int,
    val videos: VideosResponse?,
)

fun MediaEntity.mapToModel() = Detail(
    id = id,
    adult = adult,
    backdropPath = backdropPath,
    genreIds = genreIds,
    homepage = homepage,
    overview = overview,
    popularity = popularity,
    posterPath = posterPath,
    releaseDate = releaseDate ?: firstAirDate,
    mediaType = MediaType.fromString(mediaType),
    title = title ?: name ?: "",
    originalTitle = originalTitle ?: originalName ?: "",
    originCountry = originCountry,
    voteAverage = voteAverage,
    voteCount = voteCount,
    videos = videos?.results?.map { it.mapToModel() },
)