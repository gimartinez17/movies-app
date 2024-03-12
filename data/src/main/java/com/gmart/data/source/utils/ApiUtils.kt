package com.gmart.data.source.utils

import com.gmart.domain.model.Resource
import retrofit2.Response

suspend fun <T : Any> safeApiCall(apiCall: suspend () -> Response<T>): Resource<T> {
    try {
        val response = apiCall()
        if (response.isSuccessful) {
            val body = response.body()
            if (body != null) return Resource.Success(body)
        }
        response.errorBody()
        return Resource.Failure(
            code = response.code(), message = response.message()
        )
    } catch (e: Exception) {
        return Resource.Failure(e)
    }
}
