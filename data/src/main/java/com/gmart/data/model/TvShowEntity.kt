package com.gmart.data.model

import com.google.gson.annotations.SerializedName
import com.gmart.domain.model.Detail
import com.gmart.domain.model.MediaType

data class TvShowEntity(
    var id: Int,
    var adult: Boolean,
    @SerializedName("backdrop_path")
    var backdropPath: String?,
    @SerializedName("created_by")
    var createdBy: List<CreatedByEntity>?,
    var genres: List<GenreEntity>?,
    @SerializedName("genre_ids")
    var genreIds: List<Int>?,
    var homepage: String?,
    var overview: String?,
    var popularity: Float,
    @SerializedName("poster_path")
    var posterPath: String?,
    @SerializedName("first_air_date")
    var firstAirDate: String?,
    var runtime: Int?,
    var status: String?,
    @SerializedName("media_type")
    var mediaType: String?,
    var name: String?,
    @SerializedName("original_name")
    var originalName: String,
    @SerializedName("vote_average")
    var voteAverage: Float,
    @SerializedName("vote_count")
    var voteCount: Int,
    @SerializedName("number_of_seasons")
    var numberOfSeasons: Int?,
    var credits: CreditsEntity?,
    var recommendations: TvShowsResponse?,
    val videos: VideosResponse?,
)

data class CreatedByEntity(val name: String)

fun TvShowEntity.mapToModel() = Detail(
    id = id,
    adult = adult,
    backdropPath = backdropPath,
    createdBy = createdBy?.map { it.name },
    genres = genres?.map { it.mapToModel() },
    genreIds = genreIds,
    homepage = homepage,
    overview = overview,
    popularity = popularity,
    posterPath = posterPath,
    releaseDate = firstAirDate,
    runtime = runtime,
    status = status,
    mediaType = if (mediaType == null) MediaType.TV else MediaType.fromString(mediaType),
    title = name ?: "",
    originalTitle = originalName,
    voteAverage = voteAverage,
    voteCount = voteCount,
    numberOfSeasons = numberOfSeasons,
    credits = credits?.mapToModel(),
    recommendations = recommendations?.toModel()?.results,
    videos = videos?.results?.map { it.mapToModel() },
)