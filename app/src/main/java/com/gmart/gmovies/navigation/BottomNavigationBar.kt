package com.gmart.gmovies.navigation

import AppBarState
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.gmart.gmovies.utils.PreviewLayout
import com.gmart.gmovies.utils.ThemePreviews

private val mainBarRoutes = getAllMainRoutes()

@Composable
fun BottomNavigationBar(
    navController: NavHostController = rememberNavController(),
    appBarState: AppBarState = AppBarState(),
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    AnimatedVisibility(
        visible = appBarState.showBottomBar,
        enter = EnterTransition.None,
        exit = ExitTransition.None,//fadeOut(tween(300)),
        content = {
            var itemSelected by remember { mutableStateOf<MainBarRoutes>(MainBarRoutes.Home) }
            NavigationBar {
                mainBarRoutes.forEach { item ->
                    val isSelected = navBackStackEntry?.destination?.route == item.route
                    NavigationBarItem(
                        icon = { item.icon?.let { Icon(it, contentDescription = item.name) } },
                        label = { Text(text = item.name, fontSize = 9.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            indicatorColor = MaterialTheme.colorScheme.surfaceColorAtElevation(16.dp),
                        ),
                        alwaysShowLabel = true,
                        selected = isSelected,
                        onClick = {
                            itemSelected = item
                            navController.navigate(item.route) {
                                navController.graph.startDestinationRoute?.let { screenRoute ->
                                    popUpTo(screenRoute) {
                                        saveState = true
                                    }
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                    )
                }
            }
        }
    )
}

@ThemePreviews
@Composable
private fun BottomNavigationBarPreview() {
    PreviewLayout {
        BottomNavigationBar(appBarState = AppBarState(showBottomBar = true))
    }
}
