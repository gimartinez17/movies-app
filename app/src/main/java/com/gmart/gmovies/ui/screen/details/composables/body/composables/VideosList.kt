package com.gmart.gmovies.ui.screen.details.composables.body.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.gmart.domain.model.Video
import com.gmart.gmovies.ui.theme.YoutubeGray
import com.gmart.gmovies.ui.theme.md_theme_dark_onSurface
import com.gmart.gmovies.utils.AppConstants.YT_RATIO
import com.gmart.gmovies.utils.AppDimensions
import com.gmart.gmovies.utils.DeviceScreenConfiguration
import com.gmart.gmovies.utils.DeviceScreenConfiguration.DeviceScreenSize.ExtraSmall
import com.gmart.gmovies.utils.DeviceScreenConfiguration.DeviceScreenSize.Large
import com.gmart.gmovies.utils.DeviceScreenConfiguration.DeviceScreenSize.Medium
import com.gmart.gmovies.utils.DeviceScreenConfiguration.DeviceScreenSize.Small
import com.gmart.gmovies.utils.PreviewLayout
import com.gmart.gmovies.utils.ThemePreviews
import com.gmart.gmovies.utils.mockMovieDetails
import com.gmart.gmovies.utils.rememberScreenConfiguration


@Composable
fun VideosList(
    modifier: Modifier = Modifier,
    title: String,
    videos: List<Video>?,
    onClick: (String) -> Unit = {},
    configuration: DeviceScreenConfiguration = rememberScreenConfiguration(),
) {
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    var trailers = videos?.filter { it.type == "Trailer" && it.site == "YouTube" }
    if (trailers?.isEmpty() == true) trailers =
        videos?.filter { it.type == "Teaser" && it.site == "YouTube" }
    val itemWidth by remember(key1 = configuration.size) {
        mutableStateOf(
            when (configuration.size) {
                ExtraSmall, Small -> screenWidth - 48.dp
                Medium -> screenWidth - 64.dp
                Large -> screenWidth - 180.dp
            }
        )
    }

    if (trailers?.isNotEmpty() == true) {
        Column(modifier = modifier) {
            Text(
                text = title,
                modifier = Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp),
                style = MaterialTheme.typography.titleMedium
            )
            if (trailers.size > 1)
                LazyRow(
                    modifier = Modifier,
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(start = 16.dp, end = 16.dp)
                ) {
                    items(trailers.size) {
                        VideoPreview(
                            modifier = Modifier
                                .padding(top = 12.dp)
                                .width(itemWidth)
                                .shadow(6.dp, shape = MaterialTheme.shapes.medium, clip = false),
                            youtubeId = trailers[it].key,
                            onClick = onClick
                        )
                    }
                }
            else
                VideoPreview(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp, start = 16.dp, end = 16.dp)
                        .shadow(6.dp, shape = MaterialTheme.shapes.medium, clip = false),
                    youtubeId = trailers.first().key,
                    onClick = onClick
                )
        }
    }
}

@Composable
private fun VideoPreview(
    modifier: Modifier,
    youtubeId: String,
    onClick: (String) -> Unit = {},
) {
    Card(
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = AppDimensions.cardElevation),
        colors = CardDefaults.cardColors(containerColor = YoutubeGray),
        modifier = modifier
            .aspectRatio(YT_RATIO)
            .clip(MaterialTheme.shapes.medium)
            .clickable { onClick(youtubeId) },
    ) {
        Box {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data("https://img.youtube.com/vi/$youtubeId/hqdefault.jpg")
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.FillWidth,
                modifier = Modifier.fillMaxWidth(),
            )
            Icon(
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(64.dp)
                    .clip(CircleShape)
                    .clickable { onClick(youtubeId) },
                tint = md_theme_dark_onSurface,
                imageVector = Icons.Default.PlayCircle,
                contentDescription = null,
            )
        }
    }
}

@ThemePreviews
@Composable
private fun VideoListPreview() {
    PreviewLayout {
        VideosList(title = "Trailers", videos = mockMovieDetails.videos)
    }

}
