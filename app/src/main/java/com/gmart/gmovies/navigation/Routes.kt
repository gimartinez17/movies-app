package com.gmart.gmovies.navigation

import androidx.compose.material.icons.Icons.Default
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Bookmarks
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector

object NavArgs {
    const val MEDIA_ID = "media_id"
    const val GENRE_ID = "genre_id"
    const val CAST_ID = "cast_id"
    const val PERSON_ID = "person_id"
    const val VIDEO_ID = "video_id"
    const val MEDIA_TYPE = "media_type"
    const val MEDIA_LIST_TYPE = "media_list_type"
}

sealed class ScreenRoutes(val route: String, val name: String) {
    object Splash : ScreenRoutes(route = "splash_screen", name = "Splash")
    object Auth : ScreenRoutes(route = "auth_screen", name = "Authorization")
    object Config : ScreenRoutes(route = "config_screen", name = "Config")
    object Details : ScreenRoutes(route = "details_screen", name = "Details")
    object Explorer : ScreenRoutes(route = "explorer_screen", name = "Explorer")
    object Cast : ScreenRoutes(route = "cast_screen", name = "Cast")
    object Person : ScreenRoutes(route = "person_screen", name = "Person")
    object VideoPlayer : ScreenRoutes(route = "player_screen", name = "VideoPlayer")
}

sealed class MainBarRoutes(val route: String, val name: String, val icon: ImageVector? = null) {
    object Home : MainBarRoutes(route = "home_screen", name = "Home", icon = Default.Home)
    object Search : MainBarRoutes(route = "search_screen", name = "Search", icon = Default.Search)
    object Lists : MainBarRoutes(route = "lists_screen", name = "Lists", icon = Default.Bookmarks)

    object Profile :
        MainBarRoutes(route = "profile_screen", name = "Profile", icon = Default.AccountCircle)
}

fun getAllMainRoutes() = listOf(
    MainBarRoutes.Home,
    MainBarRoutes.Search,
    MainBarRoutes.Lists,
    MainBarRoutes.Profile,
)

fun getAllSecondaryRoutes() = listOf(ScreenRoutes.Details, ScreenRoutes.Auth)
