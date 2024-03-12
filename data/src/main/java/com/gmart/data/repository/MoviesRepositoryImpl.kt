package com.gmart.data.repository

import com.gmart.data.model.mapToModel
import com.gmart.data.source.remote.ApiService
import com.gmart.data.source.utils.safeApiCall
import com.gmart.domain.model.WatchProviders
import com.gmart.domain.repository.MoviesRepository
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MoviesRepositoryImpl @Inject constructor(private val api: ApiService) : MoviesRepository {

    override suspend fun getDetails(id: Int) = flow {
        val response = safeApiCall { api.getMovieDetails(id) }.mapResult { it.mapToModel() }
        emit(response)
    }

    override suspend fun getAccountStates(id: Int) = flow {
        val response = safeApiCall { api.getMovieAccountStates(id) }
            .mapResult { response -> response.mapToModel() }
        emit(response)
    }

    override suspend fun getMoviesProviders(id: Int, country: String) = flow {
        val response = safeApiCall { api.getMovieProviders(id) }
            .mapResult { response -> response.results[country]?.mapToModel() ?: WatchProviders() }
        emit(response)
    }

    override suspend fun getMovieCast(id: Int) = flow {
        val response = safeApiCall { api.getMovieCast(id) }
            .mapResult { response -> response.mapToModel().cast }
        emit(response)
    }

    override suspend fun getPopularMovies() = flow {
        val response = safeApiCall { api.getPopularMovies(1) }
            .mapResult { response -> response.results.map { it.mapToModel() } }
        emit(response)
    }

    override suspend fun getNowPlayingMovies() = flow {
        val response = safeApiCall { api.getNowPlaying(1) }
            .mapResult { response -> response.results.map { it.mapToModel() } }
        emit(response)
    }

    override suspend fun getUpcomingMovies() = flow {
        val response = safeApiCall { api.getUpcomingMovies(1) }
            .mapResult { response -> response.results.map { it.mapToModel() } }
        emit(response)
    }
}
