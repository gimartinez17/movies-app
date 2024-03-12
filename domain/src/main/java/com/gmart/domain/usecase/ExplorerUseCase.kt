package com.gmart.domain.usecase

import com.gmart.domain.model.MediaListType
import com.gmart.domain.repository.ExplorerRepository
import javax.inject.Inject

class ExplorerUseCase @Inject constructor(private val repository: ExplorerRepository) {

    suspend fun getMediaList(type: MediaListType, id: Int? = null, totalPages: Int? = null) =
        repository.getMediaList(type, id, totalPages)

}
