package com.gmart.gmovies.ui.screen.cast

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.gmart.data.source.utils.IoDispatcher
import com.gmart.domain.model.MediaType
import com.gmart.domain.model.MediaType.Companion.fromString
import com.gmart.domain.model.Resource
import com.gmart.domain.usecase.DetailUseCase
import com.gmart.gmovies.navigation.NavArgs
import com.gmart.gmovies.navigation.NavArgs.MEDIA_TYPE
import com.gmart.gmovies.ui.base.BaseViewModel
import com.gmart.gmovies.utils.logE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CastViewModel @Inject constructor(
    private val detailUseCase: DetailUseCase,
    savedStateHandle: SavedStateHandle,
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
) : BaseViewModel<CastViewState, CastEvent, CastEffect>(dispatcher) {

    private val id: Int = checkNotNull(savedStateHandle[NavArgs.CAST_ID])
    private val mediaType: MediaType = checkNotNull(fromString(savedStateHandle[MEDIA_TYPE]))

    init {
        getCast()
    }

    override fun setInitialState() = CastViewState()

    override fun handleEvents(event: CastEvent) {
        when (event) {
            CastEvent.GetCast -> getCast()
            is CastEvent.OnPersonClick -> setEffect(CastEffect.NavigateToPerson(event.id))
        }
    }

    private fun getCast() {
        viewModelScope.launch(dispatcher) {
            detailUseCase.getCast(mediaType, id).onStart {
                setViewState(CastViewState(isLoading = true))
            }.catch {
                setViewState(CastViewState(errorMessage = "Something went wrong"))
            }.collect { resource ->
                when (resource) {
                    is Resource.Success -> setViewState(CastViewState(data = resource.response))
                    is Resource.Failure -> {
                        logE(resource.error?.message.toString())
                        setViewState(CastViewState(errorMessage = resource.message))
                    }
                }
            }
        }
    }
}
