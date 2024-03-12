package com.gmart.domain.repository

import com.gmart.domain.model.AccountState
import com.gmart.domain.model.Cast
import com.gmart.domain.model.Detail
import com.gmart.domain.model.Resource
import com.gmart.domain.model.WatchProviders
import kotlinx.coroutines.flow.Flow

interface MoviesRepository {
    suspend fun getDetails(id: Int): Flow<Resource<Detail>>
    suspend fun getAccountStates(id: Int): Flow<Resource<AccountState>>
    suspend fun getMoviesProviders(id: Int, country: String): Flow<Resource<WatchProviders>>
    suspend fun getMovieCast(id: Int): Flow<Resource<List<Cast>>>

    suspend fun getPopularMovies(): Flow<Resource<List<Detail>>>
    suspend fun getNowPlayingMovies(): Flow<Resource<List<Detail>>>
    suspend fun getUpcomingMovies(): Flow<Resource<List<Detail>>>
}