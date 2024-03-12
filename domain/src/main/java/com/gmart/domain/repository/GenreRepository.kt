package com.gmart.domain.repository

import com.gmart.domain.model.Genre
import com.gmart.domain.model.Resource
import kotlinx.coroutines.flow.Flow

interface GenreRepository {
    suspend fun getMovieGenres(): Flow<Resource<List<Genre>>>
    suspend fun getTvGenres(): Flow<Resource<List<Genre>>>
}