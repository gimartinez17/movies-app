package com.gmart.gmovies.ui.screen.profile

import androidx.lifecycle.viewModelScope
import com.gmart.data.source.utils.IoDispatcher
import com.gmart.domain.model.Resource
import com.gmart.domain.usecase.UserUseCase
import com.gmart.gmovies.ui.base.BaseViewModel
import com.gmart.gmovies.utils.logE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userUseCase: UserUseCase,
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
) : BaseViewModel<ProfileViewState, ProfileEvent, Unit>(dispatcher) {

    override fun setInitialState() = ProfileViewState()

    override fun handleEvents(event: ProfileEvent) {
        when (event) {
            ProfileEvent.GetAccountDetails -> getAccountDetails()
            is ProfileEvent.SignOut -> signOut()
        }
    }

    private fun getAccountDetails() {
        viewModelScope.launch(dispatcher) {
            val accessToken = userUseCase.getAccessToken().first()
            if (accessToken.isEmpty()) {
                setViewState(ProfileViewState(showMustBeSignIn = true))
                return@launch
            }
            val accountId = userUseCase.getAccountId().first()
            userUseCase.getAccountDetails(accountId)
                .onStart { setViewState(ProfileViewState(isLoading = true)) }
                .catch { throwable ->
                    logE(throwable.message.toString())
                    setViewState(ProfileViewState(errorMessage = "Something went wrong"))
                }
                .collect { resource ->
                    when (resource) {
                        is Resource.Success -> setViewState(ProfileViewState(data = resource.response))
                        is Resource.Failure -> {
                            logE(resource.error.toString())
                            setViewState(ProfileViewState(errorMessage = "Something went wrong"))
                        }
                    }
                }
        }
    }

    private fun signOut() {
        viewModelScope.launch(dispatcher) {
            val accessToken = userUseCase.getAccessToken().first()
            userUseCase.signOut(accessToken)
                .onStart { setViewState(ProfileViewState(isLoading = true)) }
                .catch { throwable ->
                    logE(throwable.message.toString())
                    setViewState(ProfileViewState(errorMessage = "Something went wrong"))
                }
                .collect {
                    userUseCase.saveAccessToken("")
                    setViewState(ProfileViewState(showMustBeSignIn = true))
                }
        }
    }
}