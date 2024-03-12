package com.gmart.gmovies.ui.screen.search

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.gmart.data.source.utils.IoDispatcher
import com.gmart.domain.model.Detail
import com.gmart.domain.model.MediaListType
import com.gmart.domain.model.MediaType
import com.gmart.domain.usecase.ExplorerUseCase
import com.gmart.domain.usecase.SearchUseCase
import com.gmart.gmovies.ui.base.BaseViewModel
import com.gmart.gmovies.utils.logE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchMovieViewModel @Inject constructor(
    private val useCase: SearchUseCase,
    private val explorerUseCase: ExplorerUseCase,
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
) : BaseViewModel<Unit, SearchEvent, Unit>(dispatcher) {

    private val _searchResults: MutableStateFlow<PagingData<Detail>> =
        MutableStateFlow(PagingData.empty())
    private val _trending: MutableStateFlow<PagingData<Detail>> =
        MutableStateFlow(PagingData.empty())

    val searchResults: StateFlow<PagingData<Detail>> = _searchResults.asStateFlow()
    val trending: StateFlow<PagingData<Detail>> = _trending.asStateFlow()

    init {
        getTrending()
    }

    override fun setInitialState() {}

    override fun handleEvents(event: SearchEvent) {
        when (event) {
            is SearchEvent.SearchMedia -> searchMultiMedia(event.query, event.mediaType)
            SearchEvent.CleanResults -> cleanResults()
        }
    }

    private fun getTrending() {
        viewModelScope.launch(dispatcher) {
            explorerUseCase.getMediaList(MediaListType.TRENDING_ALL, totalPages = 3)
                .catch { logE(it.message.toString()) }
                .distinctUntilChanged()
                .cachedIn(viewModelScope)
                .collect { _trending.emit(it) }
        }
    }

    private fun searchMultiMedia(query: String, mediaType: MediaType) {
        viewModelScope.launch(dispatcher) {
            useCase.searchMultiMedia(query, mediaType, 1)
                .catch { logE(it.message.toString()) }
                .distinctUntilChanged()
                .cachedIn(viewModelScope)
                .collect { _searchResults.emit(it) }
        }
    }

    private fun cleanResults() {
        viewModelScope.launch(dispatcher) {
            _searchResults.emit(PagingData.empty())
        }
    }
}