package com.gmart.domain.repository

import com.gmart.domain.model.Country
import com.gmart.domain.model.Language
import com.gmart.domain.model.Resource
import kotlinx.coroutines.flow.Flow

interface ConfigRepository {

    suspend fun getPrimaryTranslations(): Flow<Resource<List<String>>>
    suspend fun getCountries(): Flow<Resource<List<Country>>>
    suspend fun getLanguages(): Flow<Resource<List<Language>>>
}