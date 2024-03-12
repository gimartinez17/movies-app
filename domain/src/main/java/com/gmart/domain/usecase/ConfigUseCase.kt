package com.gmart.domain.usecase

import com.gmart.domain.repository.ConfigRepository
import com.gmart.domain.repository.UserDataStore
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ConfigUseCase @Inject constructor(
    private val dataStore: UserDataStore,
    private val repository: ConfigRepository,
) {
    suspend fun getPrimaryTranslations() = repository.getPrimaryTranslations()
    suspend fun getCountryList() = repository.getCountries()
    suspend fun getLanguageList() = repository.getLanguages()

    suspend fun saveDarkMode(mode: String) = dataStore.saveDarkMode(mode)
    suspend fun getDarkMode(): Flow<String> = dataStore.getDarkMode()
    suspend fun saveCountry(country: String) = dataStore.saveCountry(country)
    suspend fun getCountry(): Flow<String> = dataStore.getCountry()
    suspend fun saveLanguage(language: String) = dataStore.saveLanguage(language)
    suspend fun getLanguage(): Flow<String> = dataStore.getLanguage()

}