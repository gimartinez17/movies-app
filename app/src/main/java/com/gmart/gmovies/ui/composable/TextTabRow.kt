package com.gmart.gmovies.ui.composable

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gmart.gmovies.utils.PreviewLayout
import com.gmart.gmovies.utils.ThemePreviews
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TextTabRow(
    tabs: List<String>,
    modifier: Modifier = Modifier,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    height: Dp = Dp.Unspecified,
    backgroundColor: Color = MaterialTheme.colorScheme.surfaceColorAtElevation(6.dp),
    pagerState: PagerState,
) {

    TabRow(
        selectedTabIndex = pagerState.currentPage,
        modifier = modifier,
        contentColor = backgroundColor,
        containerColor = backgroundColor,
        indicator = { },
        divider = {}
    ) {
        tabs.forEachIndexed { index, text ->
            val selected = pagerState.currentPage == index
            Tab(
                modifier = Modifier
                    .clip(CircleShape)
                    .height(height),
                selected = selected,
                onClick = { coroutineScope.launch { pagerState.animateScrollToPage(index) } },
                text = {
                    Text(
                        text = text,
                        color = if (!selected) MaterialTheme.colorScheme.onSurface.copy(alpha = 0.45F)
                        else MaterialTheme.colorScheme.onSurface,
                        fontSize = if (selected) 16.sp else 14.sp
                    )
                }
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@ThemePreviews
@Composable
private fun TabsPreview() {
    PreviewLayout {
        TextTabRow(
            listOf("MOVIES", "TV SHOWS"),
            modifier = Modifier,
            height = 32.dp,
            pagerState = rememberPagerState(pageCount = { 2 }),
        )
    }
}