package com.gmart.data.model

import com.gmart.domain.model.Credit

data class CreditsEntity(
    var cast: List<CastEntity>,
    var crew: List<CrewEntity>
)

fun CreditsEntity.mapToModel() = Credit(
    cast = cast.map { it.mapToModel() },
    crew = crew.map { it.mapToModel() }
)