package com.gmart.gmovies.ui.screen.person

import AppBarState
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gmart.domain.model.ExternalIds
import com.gmart.domain.model.MediaType
import com.gmart.domain.model.Person
import com.gmart.gmovies.R
import com.gmart.gmovies.navigation.ScreenRoutes
import com.gmart.gmovies.ui.composable.AppError
import com.gmart.gmovies.ui.composable.Loading
import com.gmart.gmovies.ui.screen.details.composables.header.DetailsHeader
import com.gmart.gmovies.ui.screen.person.composables.PersonInformation
import com.gmart.gmovies.utils.ApplicationPackage
import com.gmart.gmovies.utils.PreviewLayout
import com.gmart.gmovies.utils.ThemePreviews
import com.gmart.gmovies.utils.mockPersonDetails
import com.gmart.gmovies.utils.openApplication

@Composable
fun PersonScreen(
    id: Int,
    onComposing: (AppBarState) -> Unit = {},
    onMediaClick: (Int, MediaType) -> Unit = { _, _ -> },
    viewModel: PersonViewModel = hiltViewModel(),
) {
    val viewState by viewModel.viewState.collectAsStateWithLifecycle()
    val personEffect by viewModel.effect.collectAsStateWithLifecycle(PersonEffect.None)
    var showContrastColor by remember { mutableStateOf(false) }

    LaunchedEffect(showContrastColor) { onComposing(personAppBar(id, showContrastColor)) }

    if (viewState.isLoading) Loading(modifier = Modifier.fillMaxSize())

    if (viewState.details != null) PersonContent(
        details = viewState.details!!,
        onIconComposing = { showContrastColor = it },
        onMediaClick = { mId, type -> viewModel.setEvent(PersonEvent.OnMediaClick(mId, type)) }
    )

    if (viewState.errorMessage != null)
        AppError(onRetry = { viewModel.setEvent(PersonEvent.GetDetails(id)) })

    LaunchedEffect(personEffect) {
        when (val effect = personEffect) {
            PersonEffect.None -> {}
            is PersonEffect.NavigateToDetails -> onMediaClick(effect.id, effect.mediaType)
        }
    }
}

private fun personAppBar(id: Int, showContrastColor: Boolean) = AppBarState(
    key = ScreenRoutes.Person.route + id,
    showBackAction = true,
    showLogo = false,
    showContrastColor = showContrastColor,
)

@Composable
private fun PersonContent(
    details: Person,
    onIconComposing: (Boolean) -> Unit = {},
    onMediaClick: (Int, MediaType) -> Unit,
) {
    val configuration = LocalConfiguration.current
    val scroll: ScrollState = rememberScrollState(0)
    val headerHeight = (configuration.screenHeightDp.dp) * 2 / 3

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scroll)
    ) {
        DetailsHeader(
            title = details.name,
            imagePath = details.profilePath ?: "",
            maxHeaderHeight = headerHeight,
            onIconComposing = onIconComposing
        ) { if (details.externalIds != null) ExtraInfo(details.externalIds) }
        PersonInformation(
            modifier = Modifier
                .fillMaxHeight(),
            data = details,
            onMediaClick = onMediaClick,
        )
    }
}

@Composable
fun ExtraInfo(externalIds: ExternalIds?) {
    val context = LocalContext.current
    Column {
        Row(
            modifier = Modifier.padding(vertical = 2.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (externalIds?.twitterId != null)
                Image(
                    modifier = Modifier
                        .padding(horizontal = 2.dp)
                        .clickable {
                            context.openApplication(
                                id = externalIds.twitterId!!,
                                app = ApplicationPackage.TWITTER
                            )
                        },
                    painter = painterResource(id = R.drawable.x_icon),
                    contentDescription = null,
                )
            if (externalIds?.instagramId != null)
                Image(
                    modifier = Modifier
                        .padding(horizontal = 2.dp)
                        .clickable {
                            context.openApplication(
                                id = externalIds.instagramId!!,
                                app = ApplicationPackage.INSTAGRAM
                            )
                        },
                    painter = painterResource(id = R.drawable.instagram_icon),
                    contentDescription = null,
                )
            if (externalIds?.facebookId != null)
                Image(
                    modifier = Modifier
                        .padding(horizontal = 2.dp)
                        .clickable {
                            context.openApplication(
                                id = externalIds.facebookId!!,
                                app = ApplicationPackage.FACEBOOK
                            )
                        },
                    painter = painterResource(id = R.drawable.facebook_icon),
                    contentDescription = null,
                )
            if (externalIds?.tiktokId != null)
                Image(
                    modifier = Modifier
                        .padding(horizontal = 2.dp)
                        .clickable {
                            context.openApplication(
                                id = externalIds.tiktokId!!,
                                app = ApplicationPackage.TIKTOK
                            )
                        },
                    painter = painterResource(id = R.drawable.tiktok_icon),
                    contentDescription = null,
                )
        }
    }
}

@ThemePreviews
@Composable
private fun PersonScreenPreview() {
    PreviewLayout {
        PersonContent(details = mockPersonDetails, onMediaClick = { _, _ -> })
    }
}