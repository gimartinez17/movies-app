package com.gmart.gmovies.ui.screen.auth

import androidx.lifecycle.viewModelScope
import com.gmart.data.source.utils.IoDispatcher
import com.gmart.domain.model.Resource
import com.gmart.domain.usecase.UserUseCase
import com.gmart.gmovies.ui.base.BaseViewModel
import com.gmart.gmovies.utils.AppConstants
import com.gmart.gmovies.utils.logE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val userUseCase: UserUseCase,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : BaseViewModel<AuthViewState, AuthEvent, AuthEffect>(dispatcher) {

    init {
        createRequestToken()
    }

    override fun setInitialState() = AuthViewState()

    override fun handleEvents(event: AuthEvent) {
        when (event) {
            AuthEvent.GetRequestToken -> createRequestToken()
            is AuthEvent.GetAccessToken -> createAccessToken(event.requestToken)
        }
    }

    private fun createRequestToken() {
        viewModelScope.launch(dispatcher) {
            userUseCase.createRequestToken(AppConstants.AUTH_REDIRECTION).onStart {
                setViewState(AuthViewState(isLoading = true))
            }.catch { throwable ->
                logE(throwable.message.toString())
                setViewState(AuthViewState(errorMessage = "Something went wrong"))
            }.collect { resource ->
                when (resource) {
                    is Resource.Success -> setViewState(AuthViewState(token = resource.response))
                    is Resource.Failure -> setViewState(AuthViewState(errorMessage = "Something went wrong"))
                }
            }
        }
    }

    private fun createAccessToken(requestToken: String) {
        viewModelScope.launch(dispatcher) {
            userUseCase.createAccessToken(requestToken).onStart {
                setViewState(AuthViewState(isLoading = true))
            }.catch { throwable ->
                logE(throwable.message.toString())
                setViewState(AuthViewState(errorMessage = "Something went wrong"))
            }.collect { resource ->
                when (resource) {
                    is Resource.Success -> {
                        userUseCase.saveAccessToken(resource.response.accessToken ?: "")
                        userUseCase.saveAccountId(resource.response.accountId ?: "")
                        setEffect(AuthEffect.NavigateToPreviousScreen)
                    }

                    is Resource.Failure -> setEffect(AuthEffect.NavigateToPreviousScreen)
                }
            }
        }
    }
}