package com.gmart.gmovies.ui.screen.search.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.gmart.domain.model.Detail
import com.gmart.domain.model.MediaType
import com.gmart.gmovies.ui.composable.Loading
import com.gmart.gmovies.utils.DeviceScreenConfiguration
import com.gmart.gmovies.utils.DeviceScreenConfiguration.DeviceScreenSize.ExtraSmall
import com.gmart.gmovies.utils.DeviceScreenConfiguration.DeviceScreenSize.Large
import com.gmart.gmovies.utils.DeviceScreenConfiguration.DeviceScreenSize.Medium
import com.gmart.gmovies.utils.DeviceScreenConfiguration.DeviceScreenSize.Small
import com.gmart.gmovies.utils.rememberScreenConfiguration
import com.gmart.gmovies.utils.spamItem


@Composable
internal fun SearchResultList(
    modifier: Modifier = Modifier,
    title: String? = null,
    data: LazyPagingItems<Detail>,
    contentPadding: PaddingValues = PaddingValues(),
    onMediaClick: (Int, MediaType?) -> Unit = { _, _ -> },
    configuration: DeviceScreenConfiguration = rememberScreenConfiguration(),
) {
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val itemHeight by remember {
        mutableStateOf(
            when (configuration.size) {
                ExtraSmall, Small -> screenWidth * 2 / 7
                Medium, Large -> screenWidth * 2 / 13
            }
        )
    }
    val items by remember(key1 = configuration.size) {
        mutableIntStateOf(
            when (configuration.size) {
                ExtraSmall, Small -> 1
                Medium, Large -> 2
            }
        )
    }
    if (data.itemCount > 0)
        LazyVerticalGrid(
            modifier = modifier,
            columns = GridCells.Fixed(items),
            contentPadding = contentPadding,
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            if (title != null) spamItem {
                Text(
                    text = title.toString(),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }
            items(data.itemCount) { index ->
                val mediaData = data[index] ?: return@items
                SearchResultItem(
                    modifier = Modifier.heightIn(max = itemHeight),
                    title = mediaData.title,
                    originCountry = mediaData.originCountry,
                    releaseDate = mediaData.releaseDate,
                    backdropPath = mediaData.backdropPath,
                    score = mediaData.voteAverage,
                    onMediaClick = { onMediaClick(mediaData.id, mediaData.mediaType) }
                )
            }
            when (data.loadState.refresh) {
                is LoadState.Loading -> item { Loading(Modifier.fillMaxSize()) }
                else -> {}
            }
            when (data.loadState.append) {
                is LoadState.Loading -> item { Loading(Modifier.fillMaxSize()) }
                else -> {}
            }
        }
}
