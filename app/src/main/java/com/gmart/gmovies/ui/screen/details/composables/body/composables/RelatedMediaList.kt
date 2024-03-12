package com.gmart.gmovies.ui.screen.details.composables.body.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.gmart.domain.model.Detail
import com.gmart.domain.model.MediaType
import com.gmart.gmovies.ui.composable.ImageSize
import com.gmart.gmovies.ui.composable.MediaPoster
import com.gmart.gmovies.utils.DeviceScreenConfiguration
import com.gmart.gmovies.utils.DeviceScreenConfiguration.DeviceScreenSize.ExtraSmall
import com.gmart.gmovies.utils.DeviceScreenConfiguration.DeviceScreenSize.Large
import com.gmart.gmovies.utils.DeviceScreenConfiguration.DeviceScreenSize.Medium
import com.gmart.gmovies.utils.DeviceScreenConfiguration.DeviceScreenSize.Small
import com.gmart.gmovies.utils.rememberScreenConfiguration


@Composable
fun RelatedMediaList(
    modifier: Modifier = Modifier,
    title: String,
    list: List<Detail>?,
    onMediaClick: (Int, MediaType) -> Unit,
    configuration: DeviceScreenConfiguration = rememberScreenConfiguration(),
) {
    val screenWidth = LocalConfiguration.current.screenWidthDp
    val itemWidth by remember {
        mutableIntStateOf(
            when (configuration.size) {
                ExtraSmall, Small -> (screenWidth / 2.5).toInt()
                Medium -> screenWidth / 3
                Large -> screenWidth / 4
            }
        )
    }

    if (list?.isNotEmpty() == true) {
        Column(modifier = modifier) {
            Text(
                text = title,
                modifier = Modifier.padding(start = 16.dp, end = 16.dp),
                style = MaterialTheme.typography.titleMedium
            )
            LazyRow(
                modifier = Modifier.padding(top = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(horizontal = 16.dp)
            ) {
                items(list.size) { index ->
                    MediaPoster(
                        title = list[index].title,
                        path = list[index].posterPath,
                        modifier = Modifier
                            .width(itemWidth.dp)
                            .shadow(6.dp, shape = MaterialTheme.shapes.medium, clip = false),
                        imageSize = ImageSize.MEDIUM,
                        onMediaClick = {
                            list[index].mediaType?.let { onMediaClick(list[index].id, it) }
                        }
                    )
                }
            }
        }
    }
}
