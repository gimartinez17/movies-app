package com.gmart.gmovies.ui.screen.home.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import com.gmart.domain.model.Genre
import com.gmart.gmovies.utils.plus

@Composable
fun GenreList(
    title: String,
    genres: List<Genre>,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
    onClick: (Int, String) -> Unit,
    itemWidth: Int
) {
    Column(modifier = modifier) {
        Text(
            modifier = Modifier
                .padding(contentPadding.plus(PaddingValues(top = 12.dp, bottom = 16.dp))),
            text = title,
            style = MaterialTheme.typography.titleMedium
        )
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = contentPadding,
        ) {
            items(genres.size) { index ->
                GenreItem(
                    modifier = Modifier
                        .width(itemWidth.dp)
                        .shadow(6.dp, shape = MaterialTheme.shapes.medium, clip = false),
                    genre = genres[index],
                    onClick = onClick
                )
            }
        }
    }
}