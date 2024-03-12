package com.gmart.domain.usecase

import com.gmart.domain.repository.MoviesRepository
import javax.inject.Inject

class MoviesUseCase @Inject constructor(private val repository: MoviesRepository) {

    suspend fun getPopularMovies() = repository.getPopularMovies()
    suspend fun getNowPlayingMovies() = repository.getNowPlayingMovies()
    suspend fun getUpcomingMovies() = repository.getUpcomingMovies()
}