package com.gmart.data.model

import com.google.gson.annotations.SerializedName
import com.gmart.domain.model.Detail
import com.gmart.domain.model.MediaType

data class MovieEntity(
    var id: Int,
    var adult: Boolean,
    @SerializedName("backdrop_path")
    var backdropPath: String?,
    var genres: List<GenreEntity>?,
    @SerializedName("genre_ids")
    var genreIds: List<Int>?,
    var homepage: String?,
    var overview: String?,
    var popularity: Float,
    @SerializedName("poster_path")
    var posterPath: String?,
    @SerializedName(value = "release_date", alternate = ["first_air_date"])
    var releaseDate: String?,
    var runtime: Int?,
    var status: String?,
    @SerializedName("media_type")
    var mediaType: String?,
    @SerializedName(value = "title", alternate = ["name"])
    var title: String?,
    @SerializedName(value = "original_title", alternate = ["original_name"])
    var originalTitle: String,
    @SerializedName("vote_average")
    var voteAverage: Float,
    @SerializedName("vote_count")
    var voteCount: Int,
    @SerializedName("credits")
    var credits: CreditsEntity?,
    @SerializedName("number_of_seasons")
    var numberOfSeasons: Int?,
    var recommendations: MoviesResponse?,
    val videos: VideosResponse?,
)

fun MovieEntity.mapToModel() = Detail(
    id = id,
    adult = adult,
    backdropPath = backdropPath,
    genres = genres?.map { it.mapToModel() },
    genreIds = genreIds,
    homepage = homepage,
    overview = overview,
    popularity = popularity,
    posterPath = posterPath,
    releaseDate = releaseDate,
    runtime = runtime,
    status = status,
    mediaType = MediaType.fromString(mediaType),
    title = title ?: "",
    originalTitle = originalTitle,
    voteAverage = voteAverage,
    voteCount = voteCount,
    credits = credits?.mapToModel(),
    recommendations = recommendations?.toModel()?.results,
    videos = videos?.results?.map { it.mapToModel() },
)
