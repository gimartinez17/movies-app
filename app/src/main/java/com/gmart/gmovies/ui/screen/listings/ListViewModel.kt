package com.gmart.gmovies.ui.screen.listings

import androidx.lifecycle.viewModelScope
import com.gmart.data.source.utils.IoDispatcher
import com.gmart.domain.model.ListType
import com.gmart.domain.model.MediaType
import com.gmart.domain.model.Resource
import com.gmart.domain.model.UserList
import com.gmart.domain.usecase.ConfigUseCase
import com.gmart.domain.usecase.ListingsUseCase
import com.gmart.domain.usecase.UserUseCase
import com.gmart.gmovies.ui.base.BaseViewModel
import com.gmart.gmovies.utils.logE
import com.gmart.gmovies.utils.toDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class ListingsViewModel @Inject constructor(
    private val listingUseCase: ListingsUseCase,
    private val configUseCase: ConfigUseCase,
    private val userUseCase: UserUseCase,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : BaseViewModel<ListingViewState, ListingEvent, Unit>(dispatcher) {

    private val _listNames: MutableStateFlow<List<UserList>> = MutableStateFlow(listOf())
    val listNames: StateFlow<List<UserList>> = _listNames.asStateFlow()
    private val _selectedIndex: MutableStateFlow<Int> = MutableStateFlow(0)
    val selectedIndex: StateFlow<Int> = _selectedIndex.asStateFlow()

    private val favourites = listOf(
        UserList(id = 0, name = "", listType = ListType.FAVOURITE_MOVIES),
        UserList(id = 1, name = "", listType = ListType.FAVOURITE_TV)
    )

    override fun setInitialState(): ListingViewState = ListingViewState()

    override fun handleEvents(event: ListingEvent) {
        when (event) {
            ListingEvent.GetListings -> getListings()
            is ListingEvent.SelectList -> selectList(event.idx)
            is ListingEvent.GetListDetails -> getListDetails(event.id)
            is ListingEvent.RemoveMedia -> removeMedia(event.listId, event.mediaId, event.mediaType)
            is ListingEvent.DeleteList -> deleteList(event.listId)
            is ListingEvent.CreateList -> createList(
                event.name,
                event.description,
                event.isPublic,
                event.sortBy
            )

            is ListingEvent.EditList -> editList(
                event.listId,
                event.name,
                event.description,
                event.isPublic,
                event.sortBy
            )
        }
    }

    private fun getListings(createdList: Boolean = false) {
        viewModelScope.launch(dispatcher) {
            val accessToken = userUseCase.getAccessToken().first()
            if (accessToken.isEmpty()) {
                setViewState(ListingViewState(showMustBeSignedIn = true))
                return@launch
            }

            val accountId = userUseCase.getAccountId().first()
            listingUseCase.getMyLists(accountId)
                .onStart { updateViewState { copy(isLoading = true) } }
                .catch { throwable ->
                    logE(throwable.message.toString())
                    setViewState(ListingViewState(errorMessage = "Something went wrong"))
                }
                .collect { resource ->
                    when (resource) {
                        is Resource.Success -> {
                            val lists =
                                favourites + resource.response.sortedBy { it.createdAt?.toDate() }
                            if (_listNames.value != lists) {
                                _listNames.emit(lists)
                            }
                            if (createdList) {
                                val lastCreatedList =
                                    lists.maxByOrNull { it.createdAt?.toDate() ?: Date() }
                                val lastCreatedIndex = lists.indexOf(lastCreatedList)
                                selectList(lastCreatedIndex)
                            } else {
                                getListDetails(_listNames.value[_selectedIndex.value].id)
                            }
                        }

                        is Resource.Failure -> {
                            logE(resource.error.toString())
                            updateViewState {
                                copy(isLoading = false, snackBarMessage = "Something went wrong")
                            }
                        }
                    }
                }
        }
    }

    private fun selectList(idx: Int) {
        _selectedIndex.update { idx }
        getListDetails(listNames.value[idx].id)
    }

    private fun getListDetails(id: Int) {
        viewModelScope.launch(dispatcher) {
            val accountId = userUseCase.getAccountId().first()
            listingUseCase.getListDetails(accountId, id).onStart {
                updateViewState { copy(isLoading = true) }
            }.catch { throwable ->
                logE(throwable.message.toString())
                setViewState(ListingViewState(errorMessage = "Something went wrong"))
            }.collect { resources ->
                when (resources) {
                    is Resource.Success -> setViewState(
                        ListingViewState(
                            data = resources.response.toMutableList(),
                            listType = when (id) {
                                0 -> ListType.FAVOURITE_MOVIES
                                1 -> ListType.FAVOURITE_TV
                                else -> ListType.USER
                            }
                        )
                    )

                    is Resource.Failure -> {
                        logE(resources.error?.message.toString())
                        setViewState(ListingViewState(errorMessage = "Something went wrong"))
                    }
                }
            }
        }
    }

    private fun removeMedia(listId: Int, mediaId: Int, mediaType: MediaType) {
        when (listId) {
            0 -> removeFavourite(mediaId, mediaType)
            1 -> removeFavourite(mediaId, mediaType)
            else -> removeItem(listId, mediaId, mediaType)
        }
    }

    private fun removeFavourite(id: Int, mediaType: MediaType) {
        viewModelScope.launch(dispatcher) {
            val accountId = userUseCase.getAccountId().first()
            listingUseCase.postFavourite(id, mediaType, false, accountId).onStart {
                updateViewState { copy(isLoading = true) }
            }.catch { throwable ->
                logE(throwable.message.toString())
                updateViewState { copy(isLoading = false, errorMessage = "Something went wrong") }
            }.collect { resources ->
                when (resources) {
                    is Resource.Success -> getListDetails(listNames.value[_selectedIndex.value].id)
                    is Resource.Failure -> {
                        logE(resources.error?.message.toString())
                    }
                }
            }
        }
    }

    private fun removeItem(listId: Int, mediaId: Int, mediaType: MediaType) {
        viewModelScope.launch(dispatcher) {
            listingUseCase.removeItem(listId, mediaId, mediaType).onStart {
                updateViewState { copy(isLoading = true) }
            }.catch { throwable ->
                logE(throwable.message.toString())
                updateViewState { copy(isLoading = false, errorMessage = "Something went wrong") }
            }.collect { resources ->
                when (resources) {
                    is Resource.Success -> getListDetails(listNames.value[_selectedIndex.value].id)
                    is Resource.Failure -> {
                        logE(resources.error?.message.toString())
                        updateViewState {
                            copy(isLoading = false, snackBarMessage = "Something went wrong")
                        }
                    }
                }
            }
        }
    }

    private fun createList(name: String, description: String, isPublic: Boolean, sortBy: String) {
        viewModelScope.launch(dispatcher) {
            val country = configUseCase.getCountry().first()
            val language = configUseCase.getLanguage().first().substring(0, 2)
            listingUseCase.createList(name, description, isPublic, sortBy, language, country)
                .onStart {
                    updateViewState { copy(isLoading = true) }
                }.catch { throwable ->
                    logE(throwable.message.toString())
                    updateViewState {
                        copy(isLoading = false, snackBarMessage = "Something went wrong")
                    }
                }.collect { resources ->
                    when (resources) {
                        is Resource.Success -> getListings(createdList = true)
                        is Resource.Failure -> {
                            logE(resources.error?.message.toString())
                            updateViewState {
                                copy(isLoading = false, snackBarMessage = "Something went wrong")
                            }
                        }
                    }
                }
        }
    }

    private fun editList(
        listId: Int,
        name: String,
        description: String,
        isPublic: Boolean,
        sortBy: String
    ) {
        viewModelScope.launch(dispatcher) {
            val country = configUseCase.getCountry().first()
            val language = configUseCase.getLanguage().first().substring(0, 1)
            listingUseCase.editList(listId, name, description, isPublic, sortBy, language, country)
                .onStart {
                    updateViewState { copy(isLoading = true) }
                }.catch { throwable ->
                    logE(throwable.message.toString())
                    updateViewState {
                        copy(isLoading = false, snackBarMessage = "Something went wrong")
                    }
                }.collect { resources ->
                    when (resources) {
                        is Resource.Success -> getListings(createdList = false)
                        is Resource.Failure -> {
                            logE(resources.error?.message.toString())
                            updateViewState {
                                copy(isLoading = false, snackBarMessage = "Something went wrong")
                            }
                        }
                    }
                }
        }
    }

    private fun deleteList(listId: Int) {
        viewModelScope.launch(dispatcher) {
            listingUseCase.deleteList(listId).onStart {
                updateViewState { copy(isLoading = true) }
            }.catch { throwable ->
                logE(throwable.message.toString())
                updateViewState { copy(isLoading = false, errorMessage = "Something went wrong") }
            }.collect { resources ->
                when (resources) {
                    is Resource.Success -> {
                        _listNames.update { it.filter { list -> list.id != listId } }
                        selectList(0)
                    }

                    is Resource.Failure -> {
                        logE(resources.error?.message.toString())
                        updateViewState {
                            copy(isLoading = false, snackBarMessage = "Something went wrong")
                        }
                    }
                }
            }
        }
    }
}