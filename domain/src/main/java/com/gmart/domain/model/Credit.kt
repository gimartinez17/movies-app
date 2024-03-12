package com.gmart.domain.model

enum class CrewType(val department: String, val job: String) {
    DIRECTOR("Directing", "Director"),
    WRITER("Writing", "Writer"),
}

data class Credit(
    var cast: List<Cast>,
    var crew: List<Crew>
)

data class Cast(
    var id: Int,
    var castId: Int = 0,
    var character: String,
    var creditId: String,
    var gender: Int = 0,
    var name: String,
    var order: Int = 0,
    var profilePath: String? = null
)

data class Crew(
    var creditId: String = "",
    var department: String,
    var gender: Int = 0,
    var id: Int,
    var job: String,
    var name: String,
    var profilePath: String? = null
)
