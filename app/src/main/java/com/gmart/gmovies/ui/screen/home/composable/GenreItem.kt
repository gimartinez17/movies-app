package com.gmart.gmovies.ui.screen.home.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import com.gmart.domain.model.Genre
import com.gmart.gmovies.ui.composable.MediaBackdrop
import com.gmart.gmovies.ui.composable.ImageSize
import com.gmart.gmovies.ui.theme.DarkSurface
import com.gmart.gmovies.utils.AppDimensions
import com.gmart.gmovies.utils.PreviewLayout
import com.gmart.gmovies.utils.ThemePreviews

@Composable
fun GenreItem(
    genre: Genre,
    modifier: Modifier = Modifier,
    shape: Shape = MaterialTheme.shapes.medium,
    onClick: (Int, String) -> Unit = { _, _ -> }
) {
    Card(
        shape = shape,
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = AppDimensions.cardElevation),
        modifier = modifier
            .clip(shape)
            .clickable { onClick(genre.id, genre.name) }
            .aspectRatio(8 / 4f)
            .background(MaterialTheme.colorScheme.surface),
    ) {
        MediaBackdrop(
            path = genre.image,
            title = genre.name,
            imageSize = ImageSize.MEDIUM,
        )
    }
}

@ThemePreviews
@Composable
private fun GenreItemPreview() {
    PreviewLayout {
        GenreItem(
            genre = Genre(id = 1, name = "Action", image = null),
            modifier = Modifier.padding(16.dp)
        )
    }
}