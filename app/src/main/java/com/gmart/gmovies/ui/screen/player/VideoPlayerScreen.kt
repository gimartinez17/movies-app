package com.gmart.gmovies.ui.screen.player

import AppBarState
import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
import android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
import android.widget.FrameLayout.LayoutParams
import android.widget.FrameLayout.LayoutParams.MATCH_PARENT
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.zIndex
import com.gmart.gmovies.navigation.ScreenRoutes
import com.gmart.gmovies.ui.base.SIDE_EFFECTS_KEY
import com.gmart.gmovies.ui.composable.Loading
import com.gmart.gmovies.ui.theme.DarkSurface
import com.gmart.gmovies.ui.theme.md_theme_dark_onSurface
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.pierfrancescosoffritti.androidyoutubeplayer.core.customui.DefaultPlayerUiController
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.loadOrCueVideo
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("SourceLockedOrientationActivity")
@Composable
fun VideoPlayerScreen(
    id: String,
    onComposing: (AppBarState) -> Unit = {},
    popBackStack: () -> Unit,
) {
    val context = LocalContext.current
    val activity = LocalContext.current as Activity
    val systemUiController = rememberSystemUiController()
    var isLoading by remember { mutableStateOf(true) }
    val lifecycleOwner by rememberUpdatedState(LocalLifecycleOwner.current)

    var startSeconds by rememberSaveable { mutableFloatStateOf(0f) }
    val youtubePlayer = remember(context, id) {
        YouTubePlayerView(context).apply {
            enableAutomaticInitialization = false
            this.layoutParams = LayoutParams(MATCH_PARENT, MATCH_PARENT)
            val listener = object : AbstractYouTubePlayerListener() {
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    val uiController = DefaultPlayerUiController(this@apply, youTubePlayer)
                    uiController.showUi(true)
                    uiController.showDuration(true)
                    uiController.showFullscreenButton(false)
                    uiController.showSeekBar(true)
                    uiController.showBufferingProgress(true)
                    uiController.showCurrentTime(true)
                    uiController.showMenuButton(false)
                    uiController.showPlayPauseButton(true)
                    uiController.showVideoTitle(false)
                    uiController.showYouTubeButton(true)
                    this@apply.setCustomPlayerUi(uiController.rootView)
                    youTubePlayer.loadOrCueVideo(lifecycleOwner.lifecycle, id, startSeconds)
                }

                override fun onCurrentSecond(youTubePlayer: YouTubePlayer, second: Float) {
                    startSeconds = second
                }

                override fun onStateChange(
                    youTubePlayer: YouTubePlayer,
                    state: PlayerConstants.PlayerState
                ) {
                    when (state) {
                        PlayerConstants.PlayerState.ENDED -> {
                            activity.requestedOrientation = SCREEN_ORIENTATION_PORTRAIT
                            popBackStack.invoke()
                        }

                        PlayerConstants.PlayerState.PLAYING -> {
                            activity.requestedOrientation = SCREEN_ORIENTATION_LANDSCAPE
                            isLoading = false
                        }

                        else -> {}
                    }
                }

            }
            val options: IFramePlayerOptions = IFramePlayerOptions.Builder()
                .controls(0)
                .rel(0)
                .build()
            initialize(listener, options)
        }
    }

    LaunchedEffect(SIDE_EFFECTS_KEY) {
        onComposing(AppBarState(key = ScreenRoutes.VideoPlayer.route, showTopAppBar = false))
    }

    DisposableEffect(lifecycleOwner, youtubePlayer) {
        lifecycleOwner.lifecycle.addObserver(youtubePlayer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(youtubePlayer)
        }
    }


    Box(modifier = Modifier.fillMaxSize()) {
        VideoPlayerTopAppBar(popBackStack = {
            activity.requestedOrientation = SCREEN_ORIENTATION_PORTRAIT
            popBackStack.invoke()
        })
        AndroidView(
            modifier = Modifier.wrapContentSize(),
            factory = { youtubePlayer },
        )
        if (isLoading) Loading(
            modifier = Modifier
                .fillMaxSize()
                .zIndex(1f)
                .background(color = Color.Black)
        )
    }

    DisposableEffect(SIDE_EFFECTS_KEY) {
        systemUiController.isStatusBarVisible = false
        onDispose { systemUiController.isStatusBarVisible = true }
    }

    BackHandler {
        activity.requestedOrientation = SCREEN_ORIENTATION_PORTRAIT
        popBackStack.invoke()
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun VideoPlayerTopAppBar(popBackStack: () -> Unit) {
    CenterAlignedTopAppBar(
        modifier = Modifier.zIndex(zIndex = 1f),
        title = { },
        navigationIcon = {
            IconButton(
                onClick = popBackStack,
                modifier = Modifier
                    .statusBarsPadding()
                    .padding(8.dp)
                    .clip(CircleShape)
                    .size(36.dp)
                    .background(
                        color = DarkSurface.copy(alpha = 0.35f),
                        shape = CircleShape
                    )
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = null,
                    tint = md_theme_dark_onSurface,
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
    )
}
