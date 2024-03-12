package com.gmart.domain.usecase

import com.gmart.domain.repository.TvShowRepository
import javax.inject.Inject

class TvShowsUseCase @Inject constructor(private val repository: TvShowRepository) {

    suspend fun getPopularTvShows() = repository.getPopularTvShows()
    suspend fun getAiringToday() = repository.getAiringToday()
    suspend fun getOnAir() = repository.getOnAir()
}