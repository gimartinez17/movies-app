package com.gmart.domain.repository

import com.gmart.domain.model.AccountState
import com.gmart.domain.model.Cast
import com.gmart.domain.model.Detail
import com.gmart.domain.model.Resource
import com.gmart.domain.model.WatchProviders
import kotlinx.coroutines.flow.Flow

interface TvShowRepository {
    suspend fun getDetails(id: Int): Flow<Resource<Detail>>
    suspend fun getAccountStates(id: Int): Flow<Resource<AccountState>>
    suspend fun getTvProviders(id: Int, country: String): Flow<Resource<WatchProviders>>
    suspend fun getTvShowCast(id: Int): Flow<Resource<List<Cast>>>

    suspend fun getPopularTvShows(): Flow<Resource<List<Detail>>>
    suspend fun getAiringToday(): Flow<Resource<List<Detail>>>
    suspend fun getOnAir(): Flow<Resource<List<Detail>>>
}