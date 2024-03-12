package com.gmart.data.repository

import com.gmart.data.model.mapToModelWithImage
import com.gmart.data.source.remote.ApiService
import com.gmart.data.source.utils.safeApiCall
import com.gmart.domain.repository.GenreRepository
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GenreRepositoryImpl @Inject constructor(private val api: ApiService) : GenreRepository {
    override suspend fun getMovieGenres() = flow {
        val response = safeApiCall { api.getMovieGenres() }
            .mapResult { response ->
                response.genres.map { it.mapToModelWithImage(isMovie = true) }
            }
        emit(response)
    }

    override suspend fun getTvGenres() = flow {
        val response = safeApiCall { api.getTvGenres() }
            .mapResult { response ->
                response.genres.map { it.mapToModelWithImage(isMovie = false) }
            }
        emit(response)
    }
}