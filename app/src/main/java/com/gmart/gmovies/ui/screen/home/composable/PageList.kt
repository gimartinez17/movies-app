package com.gmart.gmovies.ui.screen.home.composable

import AppBarState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.gmart.domain.model.Detail
import com.gmart.gmovies.navigation.AppTopAppBar
import com.gmart.gmovies.navigation.BottomNavigationBar
import com.gmart.gmovies.utils.AppDimensions.bottomBarHeight
import com.gmart.gmovies.utils.AppDimensions.statusBarHeight
import com.gmart.gmovies.utils.AppDimensions.tabBarHeight
import com.gmart.gmovies.utils.AppDimensions.topAppBarHeight
import com.gmart.gmovies.utils.DeviceScreenConfiguration
import com.gmart.gmovies.utils.DeviceScreenConfiguration.DeviceScreenSize.ExtraSmall
import com.gmart.gmovies.utils.DeviceScreenConfiguration.DeviceScreenSize.Large
import com.gmart.gmovies.utils.DeviceScreenConfiguration.DeviceScreenSize.Medium
import com.gmart.gmovies.utils.DeviceScreenConfiguration.DeviceScreenSize.Small
import com.gmart.gmovies.utils.PreviewLayout
import com.gmart.gmovies.utils.ThemePreviews
import com.gmart.gmovies.utils.mockMovieDetails
import com.gmart.gmovies.utils.plus
import com.gmart.gmovies.utils.rememberScreenConfiguration

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PageList(
    modifier: Modifier = Modifier,
    details: List<Detail> = listOf(),
    onMediaClick: (Int) -> Unit = { },
    configuration: DeviceScreenConfiguration = rememberScreenConfiguration(),
) {
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val mediaPageSize by remember(key1 = configuration.size) {
        val height = getMaxHeight(configuration, screenHeight)
        mutableStateOf(height)
    }
    val contentPadding by remember(key1 = configuration.size) {
        val padding = getContentPadding(configuration, screenWidth)
        mutableStateOf(padding)
    }

    val pagerState = rememberPagerState(pageCount = { details.size }, initialPage = 1)
    HorizontalPager(
        modifier = modifier
            .heightIn(max = mediaPageSize)
            .fillMaxWidth(),
        state = pagerState,
        verticalAlignment = Alignment.Top,
        contentPadding = PaddingValues(horizontal = contentPadding),
        beyondBoundsPageCount = 2,
    ) { page ->
        ExpandedMediaPage(
            pagerState = pagerState,
            detail = details[page],
            page = page,
            onMediaClick = onMediaClick,
        )
    }
}

private fun getMaxHeight(
    configuration: DeviceScreenConfiguration,
    screenHeight: Dp
): Dp {
    val paddingValue = topAppBarHeight + bottomBarHeight + statusBarHeight
    val expandedMediaPageHeight = screenHeight - tabBarHeight - paddingValue
    return when (configuration.size) {
        Large -> expandedMediaPageHeight * 0.8f
        else -> expandedMediaPageHeight
    }
}

private fun getContentPadding(
    configuration: DeviceScreenConfiguration,
    screenWidth: Dp,
): Dp {
    return when (configuration.size) {
        ExtraSmall, Small -> screenWidth / 12
        Medium -> screenWidth / 4
        Large -> screenWidth / 3
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@ThemePreviews
@Composable
private fun PageListPreview() {
    val appBarState = AppBarState(showTopAppBar = true, showBottomBar = true, showLogo = true)
    PreviewLayout {
        Scaffold(
            topBar = { AppTopAppBar(appBarState = appBarState) },
            bottomBar = { BottomNavigationBar(appBarState = appBarState) }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding.plus(PaddingValues(top = topAppBarHeight)))
            ) {
                PageList(
                    details = List(10) { mockMovieDetails },
                    modifier = Modifier
                )
                Text(text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.")
            }
        }
    }
}