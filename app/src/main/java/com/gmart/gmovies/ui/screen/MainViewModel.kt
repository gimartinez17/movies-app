package com.gmart.gmovies.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gmart.data.source.utils.IoDispatcher
import com.gmart.domain.usecase.ConfigUseCase
import com.gmart.gmovies.utils.ConnectionState
import com.gmart.gmovies.utils.NetworkService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val networkService: NetworkService,
    private val useCase: ConfigUseCase,
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
) : ViewModel() {

    private val _themeState = MutableStateFlow("system")
    val themeState: StateFlow<String> = _themeState
    val networkStatus: StateFlow<ConnectionState> = networkService.connectionState.stateIn(
        initialValue = ConnectionState.Unknown,
        scope = viewModelScope,
        started = WhileSubscribed(5000)
    )

    init {
        getConfiguration()
    }

    private fun getConfiguration() {
        viewModelScope.launch(dispatcher) {
            useCase.getDarkMode().collect { _themeState.value = it }
        }
    }
}