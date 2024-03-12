package com.gmart.gmovies.ui.screen.cast

import AppBarState
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gmart.gmovies.navigation.ScreenRoutes
import com.gmart.gmovies.ui.base.SIDE_EFFECTS_KEY
import com.gmart.gmovies.ui.composable.AppError
import com.gmart.gmovies.ui.composable.Loading
import com.gmart.gmovies.ui.screen.cast.composables.CastGrid
import com.gmart.gmovies.utils.DeviceScreenConfiguration
import com.gmart.gmovies.utils.DeviceScreenConfiguration.DeviceScreenSize.*
import com.gmart.gmovies.utils.rememberScreenConfiguration

@Composable
fun CastScreen(
    id: Int,
    paddingValues: PaddingValues = PaddingValues(),
    onComposing: (AppBarState) -> Unit = {},
    onPersonClick: (Int) -> Unit = { },
    viewModel: CastViewModel = hiltViewModel(),
    configuration: DeviceScreenConfiguration = rememberScreenConfiguration(),
) {
    val viewState by viewModel.viewState.collectAsStateWithLifecycle()
    val castEffect by viewModel.effect.collectAsStateWithLifecycle(CastEffect.None)

    LaunchedEffect(SIDE_EFFECTS_KEY) { onComposing(castAppBar(id)) }

    val items by remember(key1 = configuration.size) {
        mutableIntStateOf(
            when (configuration.size) {
                ExtraSmall, Small -> 2
                Medium, Large -> 4
            }
        )
    }

    if (viewState.isLoading) Loading(modifier = Modifier.fillMaxSize())
    if (viewState.data != null)
        CastGrid(
            modifier = Modifier.padding(
                top = paddingValues.calculateTopPadding().plus(8.dp),
                start = 16.dp,
                end = 16.dp,
                bottom = 24.dp
            ),
            cast = viewState.data!!,
            columns = items,
            onClick = { viewModel.setEvent(CastEvent.OnPersonClick(it)) },
            listState = rememberLazyGridState(),
        )

    LaunchedEffect(castEffect) {
        when (val effect = castEffect) {
            CastEffect.None -> {}
            is CastEffect.NavigateToPerson -> onPersonClick(effect.id)
        }
    }

    if (viewState.errorMessage != null)
        AppError(onRetry = { viewModel.setEvent(CastEvent.GetCast) })
}

private fun castAppBar(id: Int) = AppBarState(
    key = ScreenRoutes.Cast.route + id,
    showBackAction = true,
    showLogo = true,
    actions = null,
)
