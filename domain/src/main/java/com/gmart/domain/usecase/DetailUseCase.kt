package com.gmart.domain.usecase

import com.gmart.domain.model.MediaType
import com.gmart.domain.model.MediaType.MOVIE
import com.gmart.domain.model.MediaType.TV
import com.gmart.domain.repository.MoviesRepository
import com.gmart.domain.repository.TvShowRepository
import javax.inject.Inject

class DetailUseCase @Inject constructor(
    private val movieRepository: MoviesRepository,
    private val tvRepository: TvShowRepository,
) {

    suspend fun getDetails(mediaType: MediaType, id: Int) = when (mediaType) {
        MOVIE -> movieRepository.getDetails(id)
        TV -> tvRepository.getDetails(id)
    }

    suspend fun getAccountStates(mediaType: MediaType, id: Int) = when (mediaType) {
        MOVIE -> movieRepository.getAccountStates(id)
        TV -> tvRepository.getAccountStates(id)
    }

    suspend fun getProviders(mediaType: MediaType, id: Int, country: String) =
        when (mediaType) {
            MOVIE -> movieRepository.getMoviesProviders(id, country)
            TV -> tvRepository.getTvProviders(id, country)
        }

    suspend fun getCast(mediaType: MediaType, id: Int) =
        when (mediaType) {
            MOVIE -> movieRepository.getMovieCast(id)
            TV -> tvRepository.getTvShowCast(id)
        }
}