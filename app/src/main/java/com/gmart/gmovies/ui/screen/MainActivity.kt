package com.gmart.gmovies.ui.screen

import AppBarState
import android.animation.ObjectAnimator
import android.graphics.Color.TRANSPARENT
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.animation.DecelerateInterpolator
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.gmart.gmovies.navigation.AppTopAppBar
import com.gmart.gmovies.navigation.BottomNavigationBar
import com.gmart.gmovies.navigation.MainNavigationGraph
import com.gmart.gmovies.ui.composable.AppNoInternetConnection
import com.gmart.gmovies.ui.theme.AppTheme
import com.gmart.gmovies.utils.ConnectionState
import dagger.hilt.android.AndroidEntryPoint

@ExperimentalAnimationApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    val viewModel: MainViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        splashAnimation()
        setContent {
            val networkStatus by viewModel.networkStatus.collectAsStateWithLifecycle()
            val configState by viewModel.themeState.collectAsStateWithLifecycle()
            var darkTheme by rememberSaveable { mutableStateOf(false) }
            val isSystemDarkTheme = isSystemInDarkTheme()

            LaunchedEffect(configState) {
                darkTheme = when (configState) {
                    "on" -> true
                    "off" -> false
                    else -> isSystemDarkTheme
                }
            }
            enableEdgeToEdge(
                SystemBarStyle.auto(TRANSPARENT, TRANSPARENT, detectDarkMode = { darkTheme })
            )

            AppTheme(darkTheme = darkTheme) {
                MainScreen(
                    modifier = Modifier.fillMaxSize(),
                    networkStatus = networkStatus,
                    navController = rememberNavController()
                )
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun splashAnimation() {

        splashScreen.setOnExitAnimationListener { splashScreenView ->
            val slideUp = ObjectAnimator.ofFloat(
                splashScreenView,
                View.TRANSLATION_Y,
                0f,
                -splashScreenView.height.toFloat()
            )
            slideUp.interpolator = DecelerateInterpolator()
            slideUp.duration = 500L
            slideUp.doOnEnd { splashScreenView.remove() }
            slideUp.start()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    modifier: Modifier,
    networkStatus: ConnectionState,
    navController: NavHostController
) {
    var appBarState by remember { mutableStateOf(AppBarState()) }
    val topAppBarState: TopAppBarState = key(appBarState.key) { rememberTopAppBarState() }
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(topAppBarState)
    val scaffoldState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = scaffoldState) },
        topBar = {
            AppTopAppBar(
                navController = navController,
                scrollBehavior = scrollBehavior,
                appBarState = appBarState,
            )
        },
        bottomBar = {
            BottomNavigationBar(
                navController = navController,
                appBarState = appBarState,
            )
        },
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
    ) { paddingValues ->
        if (networkStatus == ConnectionState.Available) MainNavigationGraph(
            navController = navController,
            paddingValues = paddingValues,
            scaffoldState = scaffoldState,
            onComposing = { appBarState = it },
        )
        if (networkStatus == ConnectionState.Unavailable) AppNoInternetConnection()
    }
}