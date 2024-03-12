package com.gmart.gmovies.ui.screen.explorer

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.gmart.data.source.utils.IoDispatcher
import com.gmart.domain.model.Detail
import com.gmart.domain.model.MediaListType.Companion.fromString
import com.gmart.domain.model.MediaListType.Companion.mediaType
import com.gmart.domain.usecase.ExplorerUseCase
import com.gmart.gmovies.navigation.NavArgs.GENRE_ID
import com.gmart.gmovies.navigation.NavArgs.MEDIA_LIST_TYPE
import com.gmart.gmovies.ui.base.BaseViewModel
import com.gmart.gmovies.utils.logE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExplorerViewModel @Inject constructor(
    private val useCase: ExplorerUseCase,
    savedStateHandle: SavedStateHandle,
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
) : BaseViewModel<PagingData<Detail>, ExplorerEvent, ExplorerEffect>(dispatcher) {

    private val mediaListType = checkNotNull(fromString(savedStateHandle[MEDIA_LIST_TYPE]))
    private val genreId: Int? = savedStateHandle[GENRE_ID]

    init {
        getMediaList()
    }

    override fun setInitialState() = PagingData.empty<Detail>()

    override fun handleEvents(event: ExplorerEvent) {
        when (event) {
            is ExplorerEvent.GetMediaList -> getMediaList()
            is ExplorerEvent.OnMediaClick -> (event.mediaType ?: mediaListType.mediaType())?.let {
                setEffect(ExplorerEffect.NavigateToDetails(event.id, it))
            }
        }
    }

    private fun getMediaList() {
        viewModelScope.launch(dispatcher) {
            useCase.getMediaList(mediaListType, genreId)
                .catch { logE(it.message.toString()) }
                .distinctUntilChanged()
                .cachedIn(viewModelScope)
                .collect { resource -> setViewState(resource) }
        }
    }
}