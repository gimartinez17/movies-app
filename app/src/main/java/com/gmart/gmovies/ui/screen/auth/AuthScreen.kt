package com.gmart.gmovies.ui.screen.auth

import AppBarState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.web.LoadingState
import com.google.accompanist.web.WebView
import com.google.accompanist.web.rememberWebViewNavigator
import com.google.accompanist.web.rememberWebViewState
import com.gmart.gmovies.navigation.ScreenRoutes
import com.gmart.gmovies.ui.base.SIDE_EFFECTS_KEY
import com.gmart.gmovies.ui.composable.AppError
import com.gmart.gmovies.ui.composable.Loading
import com.gmart.gmovies.ui.screen.auth.composables.AuthTopAppBar
import com.gmart.gmovies.utils.AppConstants

@Composable
fun AuthScreen(
    onComposing: (AppBarState) -> Unit = {},
    onBackClick: () -> Unit = {},
    viewModel: AuthViewModel = hiltViewModel(),
) {
    val authViewState by viewModel.viewState.collectAsStateWithLifecycle()
    val authEffect by viewModel.effect.collectAsState(AuthEffect.None)

    LaunchedEffect(SIDE_EFFECTS_KEY) { onComposing(authAppBar()) }

    if (authViewState.isLoading) Loading(Modifier.fillMaxSize())

    if (authViewState.token?.isNotEmpty() == true) AuthContent(
        requestToken = authViewState.token!!,
        onBackClick = { validate ->
            if (validate) viewModel.setEvent(AuthEvent.GetAccessToken(authViewState.token!!))
            else onBackClick.invoke()
        },
    )

    if (authViewState.errorMessage != null)
        AppError(onRetry = { viewModel.setEvent(AuthEvent.GetRequestToken) })

    LaunchedEffect(authEffect) {
        when (authEffect) {
            AuthEffect.None -> {}
            AuthEffect.NavigateToPreviousScreen -> onBackClick.invoke()
        }
    }
}

private fun authAppBar() = AppBarState(
    key = ScreenRoutes.Auth.route,
    showTopAppBar = false,
)

@Composable
private fun AuthContent(
    requestToken: String,
    onBackClick: (Boolean) -> Unit = {},
) {
    val url = AppConstants.AUTH_URL + requestToken
    val state = rememberWebViewState(url = AppConstants.AUTH_URL + requestToken)
    val navigator = rememberWebViewNavigator()

    LaunchedEffect(state.content.getCurrentUrl()) {
        if (state.content.getCurrentUrl() == AppConstants.AUTH_REDIRECTION) onBackClick(true)
    }

    if (state.loadingState is LoadingState.Loading) Loading(modifier = Modifier.fillMaxSize())
    Column(modifier = Modifier.fillMaxSize()) {
        AuthTopAppBar(
            url = url,
            onBackClick = { onBackClick(false) }
        )
        WebView(state = state, navigator = navigator)
    }
}
