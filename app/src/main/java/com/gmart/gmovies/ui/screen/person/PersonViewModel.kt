package com.gmart.gmovies.ui.screen.person

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.gmart.data.source.utils.IoDispatcher
import com.gmart.domain.model.Resource
import com.gmart.domain.usecase.PersonUseCase
import com.gmart.gmovies.navigation.NavArgs.PERSON_ID
import com.gmart.gmovies.ui.base.BaseViewModel
import com.gmart.gmovies.utils.logE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PersonViewModel @Inject constructor(
    private val personUseCase: PersonUseCase,
    savedStateHandle: SavedStateHandle,
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
) : BaseViewModel<PersonViewState, PersonEvent, PersonEffect>(dispatcher) {

    private val id: Int = checkNotNull(savedStateHandle[PERSON_ID])

    init {
        getDetails()
    }

    override fun setInitialState() = PersonViewState()

    override fun handleEvents(event: PersonEvent) {
        when (event) {
            is PersonEvent.GetDetails -> getDetails()
            is PersonEvent.OnMediaClick ->
                setEffect(PersonEffect.NavigateToDetails(event.id, event.mediaType))
        }
    }

    private fun getDetails() {
        viewModelScope.launch(dispatcher) {
            personUseCase.getPersonDetails(id).onStart {
                setViewState(PersonViewState(isLoading = true))
            }.catch { throwable ->
                logE(throwable.message.toString())
                setViewState(PersonViewState(errorMessage = "Something went wrong"))
            }.collect { resources ->
                when (resources) {
                    is Resource.Success -> setViewState(PersonViewState(details = resources.response))
                    is Resource.Failure -> {
                        logE(resources.error?.message.toString())
                        setViewState(PersonViewState(errorMessage = resources.message))
                    }
                }
            }
        }
    }
}