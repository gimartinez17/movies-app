package com.gmart.domain.usecase

import com.gmart.domain.model.MediaType
import com.gmart.domain.repository.SearchRepository
import javax.inject.Inject

class SearchUseCase @Inject constructor(private val repository: SearchRepository) {
    suspend fun searchMultiMedia(query: String, mediaType: MediaType, page: Int) =
        repository.searchMultiMedia(query, mediaType, page)
}