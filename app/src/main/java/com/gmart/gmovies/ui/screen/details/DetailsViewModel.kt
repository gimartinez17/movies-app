package com.gmart.gmovies.ui.screen.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.gmart.data.source.utils.IoDispatcher
import com.gmart.domain.model.MediaType
import com.gmart.domain.model.MediaType.Companion.fromString
import com.gmart.domain.model.Resource
import com.gmart.domain.usecase.ConfigUseCase
import com.gmart.domain.usecase.DetailUseCase
import com.gmart.domain.usecase.ListingsUseCase
import com.gmart.domain.usecase.UserUseCase
import com.gmart.gmovies.navigation.NavArgs.MEDIA_ID
import com.gmart.gmovies.navigation.NavArgs.MEDIA_TYPE
import com.gmart.gmovies.ui.base.BaseViewModel
import com.gmart.gmovies.utils.Quadruple
import com.gmart.gmovies.utils.combine
import com.gmart.gmovies.utils.logE
import com.gmart.gmovies.utils.toDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val userUseCase: UserUseCase,
    private val detailUseCase: DetailUseCase,
    private val configUseCase: ConfigUseCase,
    private val listingsUseCase: ListingsUseCase,
    savedStateHandle: SavedStateHandle,
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
) : BaseViewModel<DetailsViewState, DetailsEvent, DetailsEffect>(dispatcher) {

    private val id: Int = checkNotNull(savedStateHandle[MEDIA_ID])
    private val mediaType: MediaType = checkNotNull(fromString(savedStateHandle[MEDIA_TYPE]))

    init {
        getDetails()
    }

    override fun setInitialState() = DetailsViewState()

    override fun handleEvents(event: DetailsEvent) {
        when (event) {
            DetailsEvent.GetDetails -> getDetails()
            is DetailsEvent.OnFavouriteClick -> postFavourite(event.isFavourite)
            is DetailsEvent.OnCastItemClick -> setEffect(DetailsEffect.NavigateToCast(event.id))
            is DetailsEvent.OnMediaClick ->
                setEffect(DetailsEffect.NavigateToDetails(event.id, event.mediaType))

            DetailsEvent.OnViewAllCastClick ->
                setEffect(DetailsEffect.NavigateToAllCast(id, mediaType))

            is DetailsEvent.OnVideoClick ->
                setEffect(DetailsEffect.NavigateToVideoPlayerScreen(event.videoKey))

            is DetailsEvent.OnAddToListClick -> addItemToList(event.listId, id, mediaType)
        }
    }

    private fun getDetails() {
        viewModelScope.launch(dispatcher) {
            val accessToken = userUseCase.getAccessToken().first()
            val countryCode = configUseCase.getCountry().first()
            val detailsDeferred =
                async(Dispatchers.IO) { detailUseCase.getDetails(mediaType, id) }
            val accountStatesDeferred =
                async(Dispatchers.IO) { detailUseCase.getAccountStates(mediaType, id) }
            val providersDeferred =
                async(Dispatchers.IO) {
                    detailUseCase.getProviders(mediaType, id, countryCode)
                }
            val userListsDeferred = async(Dispatchers.IO) {
                listingsUseCase.getMyLists(userUseCase.getAccountId().first())
            }

            combine(
                detailsDeferred.await(),
                accountStatesDeferred.await(),
                providersDeferred.await(),
                userListsDeferred.await(),
                ::Quadruple
            ).onStart {
                setViewState(DetailsViewState(isLoading = true))
            }.catch { throwable ->
                logE(throwable.message.toString())
                setViewState(DetailsViewState(errorMessage = "Something went wrong"))
            }.collect { resources ->
                val movieResource = resources.first
                val accountStatesResource = resources.second
                val providersResource = resources.third
                val userListsResource = resources.fourth
                var movieState = DetailsViewState()

                when (movieResource) {
                    is Resource.Success ->
                        movieState = movieState.copy(details = movieResource.response)

                    is Resource.Failure -> {
                        logE(movieResource.error?.message.toString())
                        setViewState(DetailsViewState(errorMessage = "Something went wrong"))
                        return@collect
                    }
                }
                when (accountStatesResource) {
                    is Resource.Success -> if (accessToken.isNotEmpty())
                        movieState =
                            movieState.copy(accountState = accountStatesResource.response)

                    is Resource.Failure -> logE(accountStatesResource.error?.message.toString())
                }
                when (providersResource) {
                    is Resource.Success -> movieState =
                        movieState.copy(watchProviders = providersResource.response)

                    is Resource.Failure -> logE(providersResource.error?.message.toString())
                }
                when (userListsResource) {
                    is Resource.Success -> if (accessToken.isNotEmpty()) {
                        val list = userListsResource.response.sortedBy { it.createdAt?.toDate() }
                        movieState = movieState.copy(userList = list)
                    }

                    is Resource.Failure -> logE(userListsResource.error?.message.toString())
                }
                setViewState(movieState)
            }
        }
    }

    private fun postFavourite(isFavourite: Boolean) {
        viewModelScope.launch(dispatcher) {
            val accountId = userUseCase.getAccountId().first()
            listingsUseCase.postFavourite(id, mediaType, isFavourite, accountId).onStart {
                updateViewState { copy(isLoading = true) }
            }.catch { throwable ->
                updateViewState { copy(isLoading = false) }
                logE(throwable.message.toString())
            }.collect { resources ->
                when (resources) {
                    is Resource.Success -> getAccountState(id, mediaType)
                    is Resource.Failure -> {
                        logE(resources.error?.message.toString())
                        updateViewState { copy(isLoading = false) }
                    }
                }
            }
        }
    }

    private fun getAccountState(id: Int, mediaType: MediaType) {
        viewModelScope.launch(dispatcher) {
            detailUseCase.getAccountStates(mediaType, id).catch { throwable ->
                logE(throwable.message.toString())
                updateViewState { copy(isLoading = false) }
            }.collect { resources ->
                when (resources) {
                    is Resource.Success -> updateViewState {
                        copy(isLoading = false, accountState = resources.response)
                    }

                    is Resource.Failure -> {
                        logE(resources.error?.message.toString())
                        updateViewState { copy(isLoading = false) }
                    }
                }
            }
        }
    }

    private fun addItemToList(listId: Int, mediaId: Int, mediaType: MediaType) {
        viewModelScope.launch(dispatcher) {
            listingsUseCase.addItems(listId, mediaId, mediaType).onStart {
                updateViewState { copy(isLoading = true) }
            }.catch { throwable ->
                updateViewState { copy(isLoading = false) }
                logE(throwable.message.toString())
            }.collect { resources ->
                when (resources) {
                    is Resource.Success ->
                        updateViewState { copy(isLoading = false, snackBarMessage = "Item added successfully!") }
                    is Resource.Failure -> {
                        logE(resources.error?.message.toString())
                        updateViewState { copy(isLoading = false) }
                    }
                }
            }
        }
    }
}