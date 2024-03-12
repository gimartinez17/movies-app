package com.gmart.domain.model

sealed class Resource<out T : Any> {

    data class Success<out T : Any>(val response: T) : Resource<T>()

    data class Failure(
        val error: Throwable? = null,
        val code: Int = 0,
        val message: String? = null
    ) : Resource<Nothing>()

    fun <B : Any> mapResult(m: (T) -> B): Resource<B> = when (this) {
        is Success -> Success(m(response))
        is Failure -> Failure(error, code, message)
    }

}

// class Failure(val code: Int, message: String) : Throwable(message)

