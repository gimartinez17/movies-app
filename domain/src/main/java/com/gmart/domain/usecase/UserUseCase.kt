package com.gmart.domain.usecase

import com.gmart.domain.repository.AuthRepository
import com.gmart.domain.repository.UserDataStore
import javax.inject.Inject

class UserUseCase @Inject constructor(
    private val dataStore: UserDataStore,
    private val repository: AuthRepository,
) {
    suspend fun createRequestToken(redirectTo: String) = repository.createRequestToken(redirectTo)

    suspend fun createAccessToken(token: String) = repository.createAccessToken(token)
    suspend fun getAccountDetails(userName: String) = repository.getAccountDetails(userName)
    suspend fun signOut(accessToken: String) = repository.signOut(accessToken)

    suspend fun getAccountId() = dataStore.getAccountId()
    suspend fun saveAccountId(accountId: String) = dataStore.saveAccountId(accountId)
    suspend fun getAccessToken() = dataStore.getAccessToken()
    suspend fun saveAccessToken(sessionId: String) = dataStore.saveAccessToken(sessionId)
}