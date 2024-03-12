package com.gmart.gmovies.ui.screen.auth.composables

import AppBarState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.gmart.gmovies.R
import com.gmart.gmovies.ui.base.SIDE_EFFECTS_KEY
import com.gmart.gmovies.ui.composable.PoweredBy
import com.gmart.gmovies.utils.PreviewLayout
import com.gmart.gmovies.utils.ThemePreviews

@Composable
fun MustSignedInView(
    paddingValues: PaddingValues = PaddingValues(),
    onLoginClick: () -> Unit = {},
    onSettingClick: () -> Unit = {},
    onComposing: (AppBarState) -> Unit = {},
) {
    LaunchedEffect(SIDE_EFFECTS_KEY) { onComposing(mustSignedInAppBarState(onSettingClick)) }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(R.drawable.login_background_image),
            contentScale = ContentScale.Crop,
            contentDescription = "",
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.surface,
                            MaterialTheme.colorScheme.surface.copy(alpha = 0.8f),
                            MaterialTheme.colorScheme.surface.copy(alpha = 0.6f),
                        ),
                        startY = Float.POSITIVE_INFINITY,
                        endY = 0f
                    )
                )
        )
        Column(
            modifier = Modifier
                .align(alignment = Alignment.BottomCenter)
                .padding(
                    horizontal = 32.dp,
                    vertical = paddingValues.calculateBottomPadding() + 32.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = stringResource(id = R.string.sign_in_title),
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(id = R.string.sign_in_message),
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                modifier = Modifier,
                onClick = onLoginClick,
            ) {
                Text(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    text = stringResource(id = R.string.sign_in)
                )
            }
            PoweredBy(modifier = Modifier.padding(top = 16.dp))
        }
    }
}

private fun mustSignedInAppBarState(onSettingClick: () -> Unit) = AppBarState(
    key = "must_signed_in",
    showLogo = true,
    showBottomBar = true,
    actions = {
        Icon(
            modifier = Modifier
                .padding(8.dp)
                .clickable { onSettingClick() },
            imageVector = Icons.Filled.Settings,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurface,
        )
    },
)


@ThemePreviews
@Composable
fun MustBeLoggedViewPreview() {
    PreviewLayout {
        MustSignedInView()
    }
}