package com.gmart.data.repository

import com.gmart.data.model.mapToModel
import com.gmart.data.source.remote.ApiService
import com.gmart.data.source.utils.safeApiCall
import com.gmart.domain.repository.PersonRepository
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class PersonRepositoryImpl @Inject constructor(
    private val api: ApiService
) : PersonRepository {

    override suspend fun getPersonDetails(id: Int) = flow {
        val response = safeApiCall { api.gePersonDetails(id) }
            .mapResult { it.mapToModel() }
        emit(response)
    }
}