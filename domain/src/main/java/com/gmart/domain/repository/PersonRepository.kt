package com.gmart.domain.repository

import com.gmart.domain.model.Person
import com.gmart.domain.model.Resource
import kotlinx.coroutines.flow.Flow

interface PersonRepository {
    suspend fun getPersonDetails(id: Int): Flow<Resource<Person>>
}