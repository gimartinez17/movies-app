package com.gmart.data.repository

import com.gmart.data.model.mapToModel
import com.gmart.data.source.remote.ApiService
import com.gmart.data.source.utils.safeApiCall
import com.gmart.domain.repository.ConfigRepository
import kotlinx.coroutines.flow.flow

class ConfigRepositoryImpl(val api: ApiService) : ConfigRepository {

    override suspend fun getPrimaryTranslations() = flow {
        val response = safeApiCall { api.getPrimaryTranslations() }
        emit(response)
    }

    override suspend fun getCountries() = flow {
        val response = safeApiCall { api.getCountries() }
            .mapResult {
                val result = it.filter { country -> country.nativeName.isNotBlank() }
                result.map { country -> country.mapToModel() }
            }
        emit(response)
    }

    override suspend fun getLanguages() = flow {
        val response = safeApiCall { api.getLanguages() }
            .mapResult {
                val result = it.filter { language -> language.nativeName.isNotBlank() }
                    .filter { language -> !language.nativeName.contains("?") }
                result.map { language -> language.mapToModel() }
            }
        emit(response)
    }
}