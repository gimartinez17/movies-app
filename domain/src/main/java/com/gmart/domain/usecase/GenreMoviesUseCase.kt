package com.gmart.domain.usecase

import com.gmart.domain.repository.GenreRepository
import javax.inject.Inject

class GenreMoviesUseCase @Inject constructor(private val repository: GenreRepository) {
    suspend fun getMovieGenres() = repository.getMovieGenres()
    suspend fun getTvGenres() = repository.getTvGenres()
}