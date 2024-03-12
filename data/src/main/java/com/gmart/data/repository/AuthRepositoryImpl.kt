package com.gmart.data.repository

import com.gmart.data.model.AccessTokenRequest
import com.gmart.data.model.RequestTokenRequest
import com.gmart.data.model.SignOutRequest
import com.gmart.data.model.mapToModel
import com.gmart.data.source.remote.ApiService
import com.gmart.data.source.utils.safeApiCall
import com.gmart.domain.repository.AuthRepository
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val api: ApiService,
) : AuthRepository {

    override suspend fun createRequestToken(redirectTo: String) = flow {
        val response = safeApiCall { api.createRequestToken(RequestTokenRequest(redirectTo)) }
            .mapResult { it.requestToken ?: "" }
        emit(response)
    }

    override suspend fun createAccessToken(token: String) = flow {
        val response = safeApiCall { api.getAccessToken(AccessTokenRequest(token)) }
            .mapResult { it.mapToModel() }
        emit(response)
    }

    override suspend fun signOut(accessToken: String) = flow {
        val response = safeApiCall { api.signOut(SignOutRequest(accessToken)) }
            .mapResult { it.accessToken ?: "" }
        emit(response)
    }

    override suspend fun getAccountDetails(userName: String) = flow {
        val response = safeApiCall { api.getAccountDetails(userName) }.mapResult { it.mapToModel() }
        emit(response)
    }
}