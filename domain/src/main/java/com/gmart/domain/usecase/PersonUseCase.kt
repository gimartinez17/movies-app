package com.gmart.domain.usecase

import com.gmart.domain.repository.PersonRepository
import javax.inject.Inject

class PersonUseCase @Inject constructor(
    private val repository: PersonRepository
) {
    suspend fun getPersonDetails(id: Int) = repository.getPersonDetails(id)
}