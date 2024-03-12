package com.gmart.gmovies.ui.screen.profile

import AppBarState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gmart.domain.model.Account
import com.gmart.gmovies.R
import com.gmart.gmovies.navigation.MainBarRoutes
import com.gmart.gmovies.ui.base.SIDE_EFFECTS_KEY
import com.gmart.gmovies.ui.composable.AppDialog
import com.gmart.gmovies.ui.composable.AppError
import com.gmart.gmovies.ui.composable.AppOption
import com.gmart.gmovies.ui.composable.Loading
import com.gmart.gmovies.ui.composable.UserAvatar
import com.gmart.gmovies.ui.screen.auth.composables.MustSignedInView
import com.gmart.gmovies.utils.PreviewLayout
import com.gmart.gmovies.utils.ThemePreviews

@Composable
fun ProfileScreen(
    paddingValues: PaddingValues = PaddingValues(),
    onComposing: (AppBarState) -> Unit = {},
    onConfigClick: () -> Unit = {},
    onLoginClick: () -> Unit = {},
    viewModel: ProfileViewModel = hiltViewModel(),
) {
    val profileViewState by viewModel.viewState.collectAsStateWithLifecycle()

    LaunchedEffect(SIDE_EFFECTS_KEY) { viewModel.setEvent(ProfileEvent.GetAccountDetails) }

    if (profileViewState.isLoading) Loading(modifier = Modifier.fillMaxSize())

    if (profileViewState.data != null) ProfileContent(
        account = profileViewState.data!!,
        onComposing = onComposing,
        onSignOutClick = { viewModel.setEvent(ProfileEvent.SignOut) },
        onSettingsClick = onConfigClick,
    )

    if (profileViewState.showMustBeSignIn) MustSignedInView(
        paddingValues = paddingValues,
        onComposing = onComposing,
        onLoginClick = onLoginClick,
        onSettingClick = onConfigClick
    )

    if (profileViewState.errorMessage != null)
        AppError(onRetry = { viewModel.setEvent(ProfileEvent.GetAccountDetails) })
}

@Composable
fun ProfileContent(
    account: Account,
    onSignOutClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {},
    onComposing: (AppBarState) -> Unit = {},
) {
    var openDialog by remember { mutableStateOf(false) }

    LaunchedEffect(SIDE_EFFECTS_KEY) {
        onComposing(
            AppBarState(key = MainBarRoutes.Profile.route, showBottomBar = true, showLogo = true)
        )
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
    ) {
        UserAvatar(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .clip(CircleShape),
            path = account.avatarPath,
            imageSize = 125.dp,
        )
        Text(
            text = account.name ?: "",
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 16.dp),
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
        )
        Text(
            text = if (account.username != null) "@${account.username}" else "",
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 8.dp, bottom = 32.dp),
            //style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
        )
        AppOption(
            modifier = Modifier.padding(horizontal = 48.dp, vertical = 4.dp),
            icon = Icons.Default.Settings,
            text = stringResource(id = R.string.setting),
            onClick = onSettingsClick,
        )
        AppOption(
            modifier = Modifier.padding(horizontal = 48.dp, vertical = 4.dp),
            icon = Icons.Default.ExitToApp,
            text = stringResource(id = R.string.sign_out),
        ) { openDialog = true }
    }
    if (openDialog) {
        AppDialog(
            title = stringResource(id = R.string.sign_out_title),
            body = stringResource(id = R.string.sign_out_message),
            dismissText = stringResource(id = R.string.cancel),
            confirmText = stringResource(id = R.string.sign_out_confirm),
            onDismiss = { openDialog = false },
            onConfirm = {
                openDialog = false
                onSignOutClick()
            },
        )
    }
}

@ThemePreviews
@Composable
private fun ProfileContentPreview() {
    PreviewLayout {
        ProfileContent(
            account = Account(
                name = "User Name",
                username = "user_id"
            )
        )
    }
}