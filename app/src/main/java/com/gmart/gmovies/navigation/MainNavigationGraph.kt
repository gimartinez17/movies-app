package com.gmart.gmovies.navigation

import AppBarState
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.gmart.domain.model.MediaListType
import com.gmart.domain.model.MediaType
import com.gmart.gmovies.navigation.NavArgs.CAST_ID
import com.gmart.gmovies.navigation.NavArgs.GENRE_ID
import com.gmart.gmovies.navigation.NavArgs.MEDIA_ID
import com.gmart.gmovies.navigation.NavArgs.MEDIA_LIST_TYPE
import com.gmart.gmovies.navigation.NavArgs.MEDIA_TYPE
import com.gmart.gmovies.navigation.NavArgs.PERSON_ID
import com.gmart.gmovies.navigation.NavArgs.VIDEO_ID
import com.gmart.gmovies.ui.screen.auth.AuthScreen
import com.gmart.gmovies.ui.screen.cast.CastScreen
import com.gmart.gmovies.ui.screen.config.ConfigScreen
import com.gmart.gmovies.ui.screen.details.DetailsScreen
import com.gmart.gmovies.ui.screen.explorer.ExplorerScreen
import com.gmart.gmovies.ui.screen.home.HomeScreen
import com.gmart.gmovies.ui.screen.listings.ListingsScreen
import com.gmart.gmovies.ui.screen.person.PersonScreen
import com.gmart.gmovies.ui.screen.player.VideoPlayerScreen
import com.gmart.gmovies.ui.screen.profile.ProfileScreen
import com.gmart.gmovies.ui.screen.search.SearchScreen

@Composable
fun MainNavigationGraph(
    navController: NavHostController,
    scaffoldState: SnackbarHostState,
    paddingValues: PaddingValues = PaddingValues(),
    onComposing: (AppBarState) -> Unit = {},
) {
    NavHost(
        navController = navController,
        startDestination = MainBarRoutes.Home.route,
        //contentAlignment = Alignment.TopStart,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None }
    ) {
        composable(route = MainBarRoutes.Home.route) {
            HomeScreen(
                paddingValues = paddingValues,
                onComposing = onComposing,
                onMediaClick = { id, type -> navController.navigateToDetailsScreen(type, id) },
                onViewAll = { navController.navigateExplorerScreen(mediaListType = it) },
                onGenreClick = { id, type -> navController.navigateExplorerScreen(type, id) },
            )
        }
        composable(route = MainBarRoutes.Search.route) {
            SearchScreen(
                paddingValues = paddingValues,
                onComposing = onComposing,
                onMediaClick = { id, type -> navController.navigateToDetailsScreen(type, id) },
            )
        }
        composable(route = MainBarRoutes.Lists.route) {
            ListingsScreen(
                paddingValues = paddingValues,
                onComposing = onComposing,
                scaffoldState = scaffoldState,
                onLogInClick = { navController.navigateToAuthScreen() },
                onSettingClick = { navController.navigateConfigScreen() },
                onMediaClick = { id, type -> navController.navigateToDetailsScreen(type, id) },
            )
        }
        composable(route = MainBarRoutes.Profile.route) {
            ProfileScreen(
                paddingValues = paddingValues,
                onComposing = onComposing,
                onConfigClick = { navController.navigateConfigScreen() },
                onLoginClick = { navController.navigateToAuthScreen() },
            )
        }

        composable(
            route = ScreenRoutes.Auth.route,
            enterTransition = enterScreenTransition(),
            exitTransition = exitScreenTransition(),
            popEnterTransition = popEnterScreenTransition(),
            popExitTransition = popExitScreenTransition(),
        ) {
            AuthScreen(
                onComposing = onComposing,
                onBackClick = { navController.popBackStack() },
            )
        }
        composable(route = ScreenRoutes.Config.route) {
            ConfigScreen(onComposing = onComposing)
        }
        composable(
            route = ScreenRoutes.Details.route + "/{$MEDIA_TYPE}" + "/{$MEDIA_ID}",
            arguments = listOf(
                navArgument(MEDIA_ID) { type = NavType.IntType },
                navArgument(MEDIA_TYPE) { type = NavType.StringType },
            ),
            enterTransition = enterScreenTransition(),
            exitTransition = exitScreenTransition(),
            popEnterTransition = popEnterScreenTransition(),
            popExitTransition = popExitScreenTransition(),
        ) { navBackEntry ->
            DetailsScreen(
                id = navBackEntry.arguments?.getInt(MEDIA_ID) ?: 0,
                mediaType = MediaType.fromString(navBackEntry.arguments?.getString(MEDIA_TYPE)),
                onComposing = onComposing,
                scaffoldState = scaffoldState,
                onMediaClick = { id, type -> navController.navigateToDetailsScreen(type, id) },
                onCastItemClick = { id -> navController.navigateToPersonScreen(id) },
                onViewAllCastClick = { id, type -> navController.navigateCastScreen(type, id) },
                onVideoClick = { id -> navController.navigateToVideoPlayerScreen(id) },
            )
        }
        composable(
            route = ScreenRoutes.Cast.route + "/{$MEDIA_TYPE}" + "/{$CAST_ID}",
            arguments = listOf(
                navArgument(MEDIA_TYPE) { type = NavType.StringType },
                navArgument(CAST_ID) { type = NavType.IntType },
            ),
            enterTransition = enterScreenTransition(),
            exitTransition = exitScreenTransition(),
            popEnterTransition = popEnterScreenTransition(),
            popExitTransition = popExitScreenTransition(),
        ) { navBackEntry ->
            CastScreen(
                id = navBackEntry.arguments?.getInt(CAST_ID) ?: 0,
                onComposing = onComposing,
                paddingValues = paddingValues,
                onPersonClick = { id -> navController.navigateToPersonScreen(id) },
            )
        }
        composable(
            route = ScreenRoutes.Person.route + "/{$PERSON_ID}",
            arguments = listOf(navArgument(PERSON_ID) { type = NavType.IntType }),
            enterTransition = enterScreenTransition(),
            exitTransition = exitScreenTransition(),
            popEnterTransition = popEnterScreenTransition(),
            popExitTransition = popExitScreenTransition(),
        ) { navBackEntry ->
            PersonScreen(
                id = navBackEntry.arguments?.getInt(PERSON_ID) ?: 0,
                onComposing = onComposing,
                onMediaClick = { id, type -> navController.navigateToDetailsScreen(type, id) },
            )
        }
        composable(
            route = ScreenRoutes.VideoPlayer.route + "/{$VIDEO_ID}",
            arguments = listOf(navArgument(VIDEO_ID) { type = NavType.StringType }),
            enterTransition = enterScreenTransition(),
            exitTransition = exitScreenTransition(),
            popEnterTransition = popEnterScreenTransition(),
            popExitTransition = popExitScreenTransition(),
        ) { navBackEntry ->
            VideoPlayerScreen(
                id = navBackEntry.arguments?.getString(VIDEO_ID) ?: "",
                onComposing = onComposing,
                popBackStack = { navController.popBackStack() },
            )
        }
        composable(
            route = ScreenRoutes.Explorer.route + "/{$MEDIA_LIST_TYPE}",
            arguments = listOf(navArgument(MEDIA_LIST_TYPE) { type = NavType.StringType }),
            enterTransition = enterScreenTransition(),
            exitTransition = exitScreenTransition(),
            popEnterTransition = popEnterScreenTransition(),
            popExitTransition = popExitScreenTransition(),
        ) {
            ExplorerScreen(
                mediaListType = MediaListType.fromString(it.arguments?.getString(MEDIA_LIST_TYPE)),
                onComposing = onComposing,
                paddingValues = paddingValues,
                onMediaClick = { id, type -> navController.navigateToDetailsScreen(type, id) },
            )
        }
        composable(
            route = ScreenRoutes.Explorer.route + "/{$MEDIA_LIST_TYPE}" + "/{$GENRE_ID}",
            arguments = listOf(
                navArgument(MEDIA_LIST_TYPE) { type = NavType.StringType },
                navArgument(GENRE_ID) { type = NavType.IntType },
            ),
            enterTransition = enterScreenTransition(),
            exitTransition = exitScreenTransition(),
            popEnterTransition = popEnterScreenTransition(),
            popExitTransition = popExitScreenTransition(),
        ) {
            ExplorerScreen(
                mediaListType = MediaListType.fromString(it.arguments?.getString(MEDIA_LIST_TYPE)),
                onComposing = onComposing,
                paddingValues = paddingValues,
                onMediaClick = { id, type -> navController.navigateToDetailsScreen(type, id) },
            )
        }
    }
}

fun NavHostController.navigateToAuthScreen() {
    this.navigate(ScreenRoutes.Auth.route) {
        launchSingleTop = true
    }
}

fun NavHostController.navigateConfigScreen() {
    this.navigate(ScreenRoutes.Config.route) { launchSingleTop = true }
}

fun NavHostController.navigateToDetailsScreen(mediaType: MediaType, id: Int) {
    this.navigate(ScreenRoutes.Details.route + "/${mediaType.type}" + "/$id")
}

fun NavHostController.navigateCastScreen(mediaListType: MediaType, id: Int? = null) {
    this.navigate(ScreenRoutes.Cast.route + "/${mediaListType.type}" + "/$id")
}

fun NavHostController.navigateToPersonScreen(id: Int) {
    this.navigate(ScreenRoutes.Person.route + "/$id")
}

fun NavHostController.navigateToVideoPlayerScreen(id: String) {
    this.navigate(ScreenRoutes.VideoPlayer.route + "/$id")
}

fun NavHostController.navigateExplorerScreen(mediaListType: MediaListType, genreId: Int? = null) {
    val genre = genreId?.let { "/$it" } ?: ""
    this.navigate(ScreenRoutes.Explorer.route + "/${mediaListType.name}" + genre) {
        launchSingleTop = true
    }
}

private const val DURATION = 700
private fun enterScreenTransition(): AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition? =
    {
        slideIntoContainer(
            towards = AnimatedContentTransitionScope.SlideDirection.Left,
            animationSpec = tween(DURATION)
        )
    }

private fun popEnterScreenTransition(): AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition? =
    {
        slideIntoContainer(
            towards = AnimatedContentTransitionScope.SlideDirection.Companion.Right,
            animationSpec = tween(DURATION)
        )
    }

private fun exitScreenTransition(): AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition? =
    {
        slideOutOfContainer(
            towards = AnimatedContentTransitionScope.SlideDirection.Companion.Left,
            animationSpec = tween(DURATION)
        )
    }


private fun popExitScreenTransition(): AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition? =
    {
        slideOutOfContainer(
            towards = AnimatedContentTransitionScope.SlideDirection.Companion.Right,
            animationSpec = tween(DURATION)
        )
    }


