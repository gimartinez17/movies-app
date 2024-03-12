package com.gmart.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.gmart.domain.repository.UserDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserDataStoreImpl @Inject constructor(val context: Context) : UserDataStore {

    override suspend fun saveDarkMode(mode: String) {
        context.dataStore.edit { preferences ->
            preferences[DARK_MODE] = mode
        }
    }

    override suspend fun getDarkMode(): Flow<String> =
        context.dataStore.data.map { preferences ->
            preferences[DARK_MODE] ?: "system"
        }

    override suspend fun saveAccountId(accountId: String) {
        context.dataStore.edit { preferences ->
            preferences[ACCOUNT_ID] = accountId
        }
    }

    override suspend fun getAccountId(): Flow<String> =
        context.dataStore.data.map { preferences ->
            preferences[ACCOUNT_ID] ?: ""
        }

    override suspend fun saveAccessToken(accessToken: String) {
        context.dataStore.edit { preferences ->
            preferences[ACCESS_TOKEN] = accessToken
        }
    }

    override suspend fun getAccessToken(): Flow<String> =
        context.dataStore.data.map { preferences ->
            preferences[ACCESS_TOKEN] ?: ""
        }

    override suspend fun saveCountry(country: String) {
        context.dataStore.edit { preferences ->
            preferences[COUNTRY] = country
        }
    }

    override suspend fun getCountry(): Flow<String> =
        context.dataStore.data.map { preferences ->
            preferences[COUNTRY] ?: context.resources.configuration.locales.get(0).country
        }

    override suspend fun saveLanguage(language: String) {
        context.dataStore.edit { preferences ->
            preferences[LANGUAGE] = language
        }
    }

    override suspend fun getLanguage(): Flow<String> =
        context.dataStore.data.map { preferences ->
            preferences[LANGUAGE] ?: context.resources.configuration.locales.get(0).toLanguageTag()
        }

    companion object {
        val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "USER_PREFS")
        private val DARK_MODE = stringPreferencesKey("dark_mode")
        private val LANGUAGE = stringPreferencesKey("language")
        private val COUNTRY = stringPreferencesKey("country_code")
        private val ACCOUNT_ID = stringPreferencesKey("account_id")
        private val ACCESS_TOKEN = stringPreferencesKey("session_id")
    }
}