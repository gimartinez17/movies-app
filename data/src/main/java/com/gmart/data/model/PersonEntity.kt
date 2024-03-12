package com.gmart.data.model

import com.google.gson.annotations.SerializedName
import com.gmart.domain.model.ExternalIds
import com.gmart.domain.model.Person

data class PersonEntity(
    val id: Int,
    val name: String,
    val biography: String?,
    val birthday: String?,
    @SerializedName("deathday")
    val deathDay: String?,
    @SerializedName("place_of_birth")
    val placeOfBirth: String?,
    @SerializedName("profile_path")
    val profilePath: String?,
    @SerializedName("known_for_department")
    val knownForDepartment: String?,
    @SerializedName("also_known_as")
    val alsoKnownAs: List<String>?,
    @SerializedName("homepage")
    val homePage: String?,
    @SerializedName("combined_credits")
    val combinedCredits: MovieCreditsEntity?,
    @SerializedName("external_ids")
    val externalIds: ExternalIdsEntity?,
)

data class MovieCreditsEntity(
    val cast: List<MediaEntity>,
    val crew: List<MediaEntity>,
)

fun PersonEntity.mapToModel() = Person(
    id = id,
    name = name,
    biography = biography,
    birthday = birthday,
    deathDay = deathDay,
    placeOfBirth = placeOfBirth,
    profilePath = profilePath,
    knownForDepartment = knownForDepartment,
    alsoKnownAs = alsoKnownAs,
    homePage = homePage,
    castCredits = combinedCredits?.cast?.map { it.mapToModel() },
    crewCredits = combinedCredits?.crew?.map { it.mapToModel() },
    externalIds = externalIds?.mapToModel(),
)

data class ExternalIdsEntity(
    @SerializedName("imdb_id")
    val imdbId: String?,
    @SerializedName("facebook_id")
    val facebookId: String?,
    @SerializedName("instagram_id")
    val instagramId: String?,
    @SerializedName("twitter_id")
    val twitterId: String?,
    @SerializedName("tiktok_id")
    val tiktokId: String?,
    @SerializedName("youtube_id")
    val youtubeId: String?,
)

fun ExternalIdsEntity.mapToModel() = ExternalIds(
    imdbId = imdbId,
    facebookId = facebookId,
    instagramId = instagramId,
    twitterId = twitterId,
    tiktokId = tiktokId,
    youtubeId = youtubeId,
)