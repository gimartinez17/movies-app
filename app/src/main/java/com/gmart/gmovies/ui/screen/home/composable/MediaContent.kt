package com.gmart.gmovies.ui.screen.home.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.gmart.domain.model.Detail
import com.gmart.domain.model.Genre
import com.gmart.domain.model.MediaListType
import com.gmart.domain.model.MediaListType.AIRING_TODAY
import com.gmart.domain.model.MediaListType.NOW_PLAYING
import com.gmart.domain.model.MediaListType.ON_AIR
import com.gmart.domain.model.MediaListType.UPCOMING
import com.gmart.domain.model.MediaType
import com.gmart.gmovies.R
import com.gmart.gmovies.utils.DeviceScreenConfiguration
import com.gmart.gmovies.utils.DeviceScreenConfiguration.DeviceScreenSize.ExtraSmall
import com.gmart.gmovies.utils.DeviceScreenConfiguration.DeviceScreenSize.Large
import com.gmart.gmovies.utils.DeviceScreenConfiguration.DeviceScreenSize.Medium
import com.gmart.gmovies.utils.DeviceScreenConfiguration.DeviceScreenSize.Small
import com.gmart.gmovies.utils.PreviewLayout
import com.gmart.gmovies.utils.ThemePreviews
import com.gmart.gmovies.utils.mockGenreData
import com.gmart.gmovies.utils.mockMovieDetails
import com.gmart.gmovies.utils.rememberScreenConfiguration

@Composable
fun MediaContent(
    modifier: Modifier = Modifier,
    mediaType: MediaType,
    popular: List<Detail>? = null,
    nowPlaying: List<Detail>? = null,
    upComing: List<Detail>? = null,
    genres: List<Genre>? = null,
    onMediaClick: (Int) -> Unit = {},
    onGenreClick: (Int, String) -> Unit = { _, _ -> },
    onViewAll: (MediaListType) -> Unit = {},
    configuration: DeviceScreenConfiguration = rememberScreenConfiguration(),
) {
    val isMovie = mediaType == MediaType.MOVIE
    val screenWidth = LocalConfiguration.current.screenWidthDp
    val itemWidth by remember {
        mutableIntStateOf(
            when (configuration.size) {
                ExtraSmall, Small -> (screenWidth / 2.5).toInt()
                Medium -> (screenWidth / 3.5).toInt()
                Large -> screenWidth / 5
            }
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(bottom = 16.dp)
    ) {
        if (popular != null) PageList(
            details = popular,
            onMediaClick = onMediaClick,
        )

        val padding = PaddingValues(horizontal = 16.dp)
        val rowModifier = Modifier
            .padding(vertical = 8.dp)
            .fillMaxWidth()
            .wrapContentHeight()

        if (upComing != null) BannerList(
            title = if (isMovie) stringResource(id = R.string.upcoming_movies)
            else stringResource(id = R.string.on_air_tv_shows),
            details = upComing,
            modifier = rowModifier,
            contentPadding = padding,
            onViewAllClick = { onViewAll(if (isMovie) UPCOMING else ON_AIR) },
            onMediaClick = onMediaClick,
            itemWidth = itemWidth * 2
        )

        if (nowPlaying != null) PosterList(
            title = if (isMovie) stringResource(id = R.string.now_playing_movies)
            else stringResource(id = R.string.airing_today_tv_shows),
            details = nowPlaying,
            modifier = rowModifier,
            contentPadding = padding,
            onViewAllClick = {
                onViewAll(if (isMovie) NOW_PLAYING else AIRING_TODAY)
            },
            onMediaClick = onMediaClick,
            itemWidth = itemWidth
        )

        if (!genres.isNullOrEmpty()) GenreList(
            title = stringResource(id = R.string.explore_genres),
            genres = genres,
            modifier = rowModifier,
            contentPadding = padding,
            onClick = onGenreClick,
            itemWidth = itemWidth * 2
        )
    }
}

@ThemePreviews
@Composable
private fun MediaContentPreview() {
    PreviewLayout {
        MediaContent(
            mediaType = MediaType.MOVIE,
            popular = List(10) { mockMovieDetails },
            nowPlaying = List(10) { mockMovieDetails },
            upComing = List(10) { mockMovieDetails },
            genres = List(10) { mockGenreData },
        )
    }
}
