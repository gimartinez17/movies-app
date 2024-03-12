package com.gmart.gmovies.utils

import com.gmart.domain.model.Resource
import retrofit2.HttpException
import java.io.IOException
import java.net.HttpURLConnection
import java.net.SocketTimeoutException

fun getErrorMessage(resource: Resource.Failure): String {

    return when (val throwable = resource.error) {
        is SocketTimeoutException,
        is IOException -> "Ha ocurrido un error. Vuelve a intentarlo más tarde"
        is HttpException -> {
            when (throwable.code()) {
                HttpURLConnection.HTTP_BAD_REQUEST,
                HttpURLConnection.HTTP_UNAUTHORIZED,
                HttpURLConnection.HTTP_NOT_FOUND,
                HttpURLConnection.HTTP_FORBIDDEN,
                HttpURLConnection.HTTP_UNAVAILABLE,
                HttpURLConnection.HTTP_CLIENT_TIMEOUT -> "Ha ocurrido un error. Vuelve a intentarlo más tarde"
                else -> "Ha ocurrido un error inesperado"
            }
        }
        else -> "Ha ocurrido un error inesperado"
    }
}