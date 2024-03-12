package com.gmart.gmovies.ui.screen.details

import AppBarState
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gmart.domain.model.AccountState
import com.gmart.domain.model.MediaType
import com.gmart.domain.model.UserList
import com.gmart.gmovies.navigation.ScreenRoutes
import com.gmart.gmovies.ui.composable.AppError
import com.gmart.gmovies.ui.composable.ErrorSnackBarMessage
import com.gmart.gmovies.ui.composable.Loading
import com.gmart.gmovies.ui.screen.details.composables.body.DetailsBody
import com.gmart.gmovies.ui.screen.details.composables.header.DetailsHeader
import com.gmart.gmovies.ui.screen.details.composables.header.composables.AddToListIcon
import com.gmart.gmovies.ui.screen.details.composables.header.composables.FavouriteIcon
import com.gmart.gmovies.ui.screen.details.composables.header.composables.HeaderInformation
import com.gmart.gmovies.ui.screen.details.composables.header.composables.Score
import com.gmart.gmovies.utils.DeviceScreenConfiguration
import com.gmart.gmovies.utils.PreviewLayout
import com.gmart.gmovies.utils.ThemePreviews
import com.gmart.gmovies.utils.getHoursAndMinutes
import com.gmart.gmovies.utils.getYear
import com.gmart.gmovies.utils.joinWithComa
import com.gmart.gmovies.utils.mockMovieDetails
import com.gmart.gmovies.utils.mockWatchProviders
import com.gmart.gmovies.utils.rememberScreenConfiguration

@Composable
fun DetailsScreen(
    id: Int,
    mediaType: MediaType?,
    onComposing: (AppBarState) -> Unit = {},
    scaffoldState: SnackbarHostState,
    onMediaClick: (Int, MediaType) -> Unit = { _, _ -> },
    onCastItemClick: (Int) -> Unit = {},
    onViewAllCastClick: (Int, MediaType) -> Unit = { _, _ -> },
    onVideoClick: (String) -> Unit = {},
    viewModel: DetailsViewModel = hiltViewModel(),
) {
    val viewState by viewModel.viewState.collectAsStateWithLifecycle()
    val detailsEffect by viewModel.effect.collectAsStateWithLifecycle(DetailsEffect.None)
    var showContrastColor by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(viewState.accountState, showContrastColor) {
        val isFavorite = viewState.accountState?.favorite == true
        onComposing(
            detailsAppBar(
                id = id,
                showContrastColor = showContrastColor,
                showFavouriteIcon = viewState.accountState != null,
                isFavorite = isFavorite,
                onFavouriteClicked = { viewModel.setEvent(DetailsEvent.OnFavouriteClick(isFavorite.not())) },
                showUserList = viewState.userList?.isNotEmpty() == true,
                lists = viewState.userList ?: emptyList(),
                onMenuItemClick = { listId ->
                    viewModel.setEvent(DetailsEvent.OnAddToListClick(listId))
                }
            )
        )
    }

    if (viewState.isLoading) Loading(modifier = Modifier.fillMaxSize())

    if (viewState.details != null) DetailsContent(
        viewState = viewState,
        mediaType = mediaType,
        onMediaClick = { mId, type -> viewModel.setEvent(DetailsEvent.OnMediaClick(mId, type)) },
        onCastItemClick = { cId -> viewModel.setEvent(DetailsEvent.OnCastItemClick(cId)) },
        onViewAllCastClick = { viewModel.setEvent(DetailsEvent.OnViewAllCastClick) },
        onVideoClick = { viewModel.setEvent(DetailsEvent.OnVideoClick(it)) },
        onIconComposing = { showContrastColor = it },
    )

    if (viewState.errorMessage != null)
        AppError(onRetry = { viewModel.setEvent(DetailsEvent.GetDetails) })

    if (viewState.snackBarMessage != null) {
        ErrorSnackBarMessage(
            scaffoldState = scaffoldState,
            message = viewState.snackBarMessage!!,
            scope = scope,
        )
    }

    LaunchedEffect(detailsEffect) {
        when (val effect = detailsEffect) {
            DetailsEffect.None -> {}
            is DetailsEffect.NavigateToDetails -> onMediaClick(effect.id, effect.mediaType)
            is DetailsEffect.NavigateToCast -> onCastItemClick(effect.id)
            is DetailsEffect.NavigateToAllCast -> onViewAllCastClick(effect.id, effect.mediaType)
            is DetailsEffect.NavigateToVideoPlayerScreen -> onVideoClick(effect.videoKey)
        }
    }
}

private fun detailsAppBar(
    id: Int,
    showContrastColor: Boolean,
    showFavouriteIcon: Boolean,
    isFavorite: Boolean,
    lists: List<UserList> = emptyList(),
    onFavouriteClicked: () -> Unit = {},
    showUserList: Boolean = false,
    onMenuItemClick: (Int) -> Unit = {},
) = AppBarState(
    key = ScreenRoutes.Details.route + id,
    showBackAction = true,
    showLogo = false,
    showContrastColor = showContrastColor,
    actions = {
        if (showUserList) AddToListIcon(
            lists = lists,
            onMenuItemClick = onMenuItemClick,
            showContrastColor = showContrastColor
        )
        if (showFavouriteIcon) FavouriteIcon(
            onClick = onFavouriteClicked,
            showContrastColor = showContrastColor,
            isFavorite = isFavorite
        )
    }
)

@Composable
fun DetailsContent(
    viewState: DetailsViewState,
    mediaType: MediaType?,
    onMediaClick: (Int, MediaType) -> Unit = { _, _ -> },
    onViewAllCastClick: () -> Unit = {},
    onCastItemClick: (Int) -> Unit = {},
    onVideoClick: (String) -> Unit = {},
    configuration: DeviceScreenConfiguration = rememberScreenConfiguration(),
    onIconComposing: (Boolean) -> Unit = {},
) {
    val context = LocalContext.current
    val screenHeightDp = LocalConfiguration.current.screenHeightDp
    val scroll: ScrollState = rememberScrollState(0)
    val headerHeight by remember(key1 = configuration.size) {
        mutableStateOf(screenHeightDp.dp * 5 / 12)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .verticalScroll(scroll)
    ) {
        DetailsHeader(
            title = viewState.details!!.title,
            imagePath = viewState.details.backdropPath ?: "",
            maxHeaderHeight = headerHeight,
            onIconComposing = onIconComposing,
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                HeaderInformation(
                    modifier = Modifier.padding(vertical = 4.dp),
                    info = listOf(
                        viewState.details.releaseDate?.getYear(context),
                        viewState.details.genres?.map { it.name }?.joinWithComa(2),
                        viewState.details.runtime?.getHoursAndMinutes(),
                    )
                )
                Score(score = viewState.details.voteAverage)
            }
        }

        DetailsBody(
            media = viewState,
            mediaType = mediaType,
            modifier = Modifier.fillMaxHeight(),
            onMediaClick = onMediaClick,
            onCastItemClick = onCastItemClick,
            onViewAllCastClick = onViewAllCastClick,
            onVideoClick = onVideoClick,
        )
    }
}

@ThemePreviews
@Composable
private fun DetailsContentPreview() {
    PreviewLayout {
        DetailsContent(
            viewState = DetailsViewState(
                details = mockMovieDetails,
                accountState = AccountState(id = 0, favorite = true),
                watchProviders = mockWatchProviders,
            ),
            mediaType = MediaType.MOVIE,
        )
    }
}
