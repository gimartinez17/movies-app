package com.gmart.gmovies.navigation

import AppBarState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.PlaylistAdd
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.TopAppBarState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.gmart.gmovies.R
import com.gmart.gmovies.ui.theme.isDarkMode
import com.gmart.gmovies.utils.PreviewLayout
import com.gmart.gmovies.utils.ThemePreviews


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopAppBar(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    scrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(),
    backgroundColor: Color? = null,
    appBarState: AppBarState = AppBarState(),
) {
    if (appBarState.showTopAppBar)
        CenterAlignedTopAppBar(
            modifier = modifier.zIndex(zIndex = 1f),
            title = {
                if (appBarState.title.isNotBlank())
                    Text(text = appBarState.title)
                else if (appBarState.showLogo)
                    Image(
                        modifier = Modifier
                            .scale(0.65F)
                            .zIndex(1f)
                            .padding(8.dp),
                        painter = if (MaterialTheme.colorScheme.isDarkMode())
                            painterResource(R.drawable.app_logo_night)
                        else
                            painterResource(R.drawable.app_logo_light),
                        contentDescription = "",
                    )
            },
            navigationIcon = {
                if (appBarState.showBackAction) IconButton(
                    onClick = {
                        if (appBarState.popBackStack != null) appBarState.popBackStack.invoke()
                        else navController.popBackStack()
                    },
                    modifier = Modifier
                        .statusBarsPadding()
                        .padding(8.dp)
                        .clip(CircleShape)
                        .size(36.dp)
                        .background(
                            color = if (appBarState.showContrastColor)
                                MaterialTheme.colorScheme.surface.copy(alpha = 0.25f)
                            else
                                Color.Transparent,
                            shape = CircleShape
                        )
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurface,
                    )
                }
            },
            actions = { appBarState.actions?.invoke(this) },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = backgroundColor ?: Color.Transparent,
            ),
            scrollBehavior = scrollBehavior
        )
}

@OptIn(ExperimentalMaterial3Api::class)
@ThemePreviews
@Composable
fun TopAppBarWithLogoPreview() {
    val topAppBarState: TopAppBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(topAppBarState)
    val inverseColor = true
    PreviewLayout {
        AppTopAppBar(
            modifier = Modifier,
            navController = rememberNavController(),
            scrollBehavior = scrollBehavior,
            appBarState = AppBarState(
                key = "key",
                showBackAction = true,
                showLogo = false,
                showContrastColor = inverseColor,
                actions = {
                    IconButton(
                        onClick = {},
                        modifier = Modifier
                            .statusBarsPadding()
                            .padding(8.dp)
                            .clip(CircleShape)
                            .size(36.dp)
                            .background(
                                color = if (inverseColor)
                                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.25f)
                                else
                                    Color.Transparent,
                                shape = CircleShape
                            )
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlaylistAdd,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.surface
                        )
                    }
                    IconButton(
                        onClick = {},
                        modifier = Modifier
                            .statusBarsPadding()
                            .padding(8.dp)
                            .clip(CircleShape)
                            .size(36.dp)
                            .background(
                                color = if (inverseColor)
                                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.25f)
                                else
                                    Color.Transparent,
                                shape = CircleShape
                            )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.surface
                        )
                    }
                }
            )
        )
    }
}