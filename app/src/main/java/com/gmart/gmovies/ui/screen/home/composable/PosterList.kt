package com.gmart.gmovies.ui.screen.home.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.gmart.domain.model.Detail
import com.gmart.gmovies.R
import com.gmart.gmovies.ui.composable.ImageSize
import com.gmart.gmovies.ui.composable.MediaPoster
import com.gmart.gmovies.utils.PreviewLayout
import com.gmart.gmovies.utils.ThemePreviews
import com.gmart.gmovies.utils.mockMovieDetails
import com.gmart.gmovies.utils.plus

@Composable
fun PosterList(
    title: String,
    details: List<Detail>,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
    onViewAllClick: () -> Unit,
    onMediaClick: (Int) -> Unit,
    itemWidth: Int,
) {
    val reducedList = details.take(30)
    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .padding(contentPadding.plus(PaddingValues(top = 12.dp, bottom = 8.dp))),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = title,
                style = MaterialTheme.typography.titleMedium
            )
            TextButton(onClick = onViewAllClick) {
                Text(text = stringResource(id = R.string.view_all))
            }
        }
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = contentPadding,
        ) {
            items(reducedList.size) { index ->
                MediaPoster(
                    title = reducedList[index].title,
                    path = reducedList[index].posterPath,
                    modifier = Modifier
                        .width(itemWidth.dp)
                        .shadow(6.dp, shape = MaterialTheme.shapes.medium, clip = false),
                    imageSize = ImageSize.MEDIUM,
                    onMediaClick = { onMediaClick(reducedList[index].id) }
                )
            }
        }
    }
}

@ThemePreviews
@Composable
private fun PosterListPreview() {
    PreviewLayout {
        PosterList(
            title = "Popular",
            details = listOf(mockMovieDetails),
            onViewAllClick = {},
            onMediaClick = {},
            itemWidth = 150
        )
    }
}