package com.gmart.gmovies.utils

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridItemScope
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.runtime.Composable
import androidx.paging.compose.LazyPagingItems


fun LazyGridState.reachedBottom(buffer: Int = 1): Boolean {
    val lastVisibleItem = this.layoutInfo.visibleItemsInfo.lastOrNull()
    return lastVisibleItem?.index != 0 && lastVisibleItem?.index == this.layoutInfo.totalItemsCount - buffer
}

fun LazyGridScope.spamItem(content: @Composable LazyGridItemScope.() -> Unit) {
    item(span = { GridItemSpan(this.maxLineSpan) }, content = content)
}

@ExperimentalFoundationApi
fun <T : Any> LazyGridScope.pagingItems(
    lazyPagingItems: LazyPagingItems<T>,
    itemContent: @Composable LazyGridItemScope.(item: T?) -> Unit
) {
    items(lazyPagingItems.itemCount) { index ->
        itemContent(lazyPagingItems[index])
    }
}
