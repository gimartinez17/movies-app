package com.gmart.data.source.remote

import android.content.Context
import com.gmart.data.source.utils.Authorization
import com.gmart.data.source.utils.RegionFilterRequired
import com.gmart.domain.repository.UserDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import retrofit2.Invocation


class MoviesInterceptor(
    private val dataStore: UserDataStore,
    private val context: Context,
    private val apiToken: String
) :
    Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val invocation = originalRequest.tag(Invocation::class.java)
        val country: String = runBlocking { dataStore.getCountry().first() }
        val language: String = runBlocking { dataStore.getLanguage().first() }

        val authorizationRequired = invocation?.method()?.getAnnotation(Authorization::class.java)
        val accessToken: String = runBlocking { dataStore.getAccessToken().first() }
        val token = if (authorizationRequired != null) accessToken else apiToken
        val requestWithHeader = originalRequest
            .newBuilder()
            .addHeader("Authorization", "Bearer $token").build()

        val requestWithQueryParams = requestWithHeader.url
            .newBuilder()
            .addQueryParameter("language", language)
            .addQueryParameter("include_adult", "false")

        val regionFilterRequired =
            invocation?.method()?.getAnnotation(RegionFilterRequired::class.java)
        if (regionFilterRequired != null)
            requestWithQueryParams.addQueryParameter("watch_region", country)

        val newRequest = requestWithHeader.newBuilder()
            .url(requestWithQueryParams.build())
            .build()

        return chain.proceed(newRequest)
    }
}