package com.gmart.domain.model

data class Person(
    val id: Int,
    val name: String,
    val biography: String?,
    val birthday: String?,
    val deathDay: String? = null,
    val placeOfBirth: String? = null,
    val profilePath: String? = null,
    val knownForDepartment: String? = null,
    val alsoKnownAs: List<String>? = null,
    val homePage: String? = null,
    val castCredits: List<Detail>? = null,
    val crewCredits: List<Detail>? = null,
    val externalIds: ExternalIds? = null,
)

data class ExternalIds(
    val imdbId: String? = null,
    val facebookId: String? = null,
    val instagramId: String? = null,
    val twitterId: String? = null,
    val tiktokId: String? = null,
    val youtubeId: String? = null,
)