package com.gmart.gmovies.ui.screen.details.composables.header.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
internal fun FavouriteIcon(
    onClick: () -> Unit,
    showContrastColor: Boolean,
    isFavorite: Boolean
) {
    IconButton(
        onClick = onClick,
        modifier = Modifier
            .statusBarsPadding()
            .padding(8.dp)
            .clip(CircleShape)
            .size(36.dp)
            .background(
                color = if (showContrastColor)
                    MaterialTheme.colorScheme.surface.copy(alpha = 0.25f)
                else
                    Color.Transparent,
                shape = CircleShape
            )
    ) {
        Icon(
            imageVector = Icons.Default.Favorite,
            contentDescription = null,
            tint = if (isFavorite) MaterialTheme.colorScheme.primary
            else MaterialTheme.colorScheme.onSurface,
        )
    }
}