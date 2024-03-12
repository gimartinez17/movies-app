package com.gmart.data.repository

import com.gmart.data.model.mapToModel
import com.gmart.data.source.remote.ApiService
import com.gmart.data.source.utils.safeApiCall
import com.gmart.domain.model.WatchProviders
import com.gmart.domain.repository.TvShowRepository
import kotlinx.coroutines.flow.flow
import java.time.LocalDate
import javax.inject.Inject

class TvShowRepositoryImpl @Inject constructor(private val api: ApiService) :
    TvShowRepository {

    override suspend fun getDetails(id: Int) = flow {
        val response = safeApiCall { api.getTvShowDetails(id) }.mapResult { it.mapToModel() }
        emit(response)
    }

    override suspend fun getAccountStates(id: Int) = flow {
        val response = safeApiCall { api.getTvShowAccountStates(id) }
            .mapResult { response -> response.mapToModel() }
        emit(response)
    }

    override suspend fun getTvProviders(id: Int, country: String) = flow {
        val response = safeApiCall { api.getTvProviders(id) }
            .mapResult { response -> response.results[country]?.mapToModel() ?: WatchProviders() }
        emit(response)
    }

    override suspend fun getTvShowCast(id: Int) = flow {
        val response = safeApiCall { api.getTvCast(id) }
            .mapResult { response -> response.mapToModel().cast }
        emit(response)
    }

    override suspend fun getPopularTvShows() = flow {
        val response = safeApiCall { api.getPopularTv(1) }
            .mapResult { response -> response.results.map { it.mapToModel() } }
        emit(response)
    }

    override suspend fun getAiringToday() = flow {
        val gte = LocalDate.now().plusDays(1).toString()
        val lte = LocalDate.now().plusDays(8).toString()
        val response = safeApiCall { api.getAiringToday(1, gte, lte) }
            .mapResult { response -> response.results.map { it.mapToModel() } }
        emit(response)
    }

    override suspend fun getOnAir() = flow {
        val gte = LocalDate.now().plusDays(1).toString()
        val response = safeApiCall { api.getOnTheAir(1, gte, gte) }
            .mapResult { response -> response.results.map { it.mapToModel() } }
        emit(response)
    }
}
