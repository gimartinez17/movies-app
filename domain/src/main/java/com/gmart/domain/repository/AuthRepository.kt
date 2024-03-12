package com.gmart.domain.repository

import com.gmart.domain.model.Account
import com.gmart.domain.model.Authorization
import com.gmart.domain.model.Resource
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun createRequestToken(redirectTo: String): Flow<Resource<String>>
    suspend fun createAccessToken(token: String): Flow<Resource<Authorization>>
    suspend fun signOut(accessToken: String): Flow<Resource<String>>

    suspend fun getAccountDetails(userName: String): Flow<Resource<Account>>
}