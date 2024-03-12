package com.gmart.domain.repository

import kotlinx.coroutines.flow.Flow

interface UserDataStore {

    suspend fun saveAccountId(accountId: String)
    suspend fun getAccountId(): Flow<String>
    suspend fun saveAccessToken(accessToken: String)
    suspend fun getAccessToken(): Flow<String>

    suspend fun saveDarkMode(mode: String)
    suspend fun getDarkMode(): Flow<String>
    suspend fun saveCountry(country: String)
    suspend fun getCountry(): Flow<String>
    suspend fun saveLanguage(language: String)
    suspend fun getLanguage(): Flow<String>
}