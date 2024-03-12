package com.gmart.gmovies.ui.screen.details.composables.header.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@Composable
fun DetailsDot(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .padding(horizontal = 4.dp)
            .size(4.dp)
            .clip(CircleShape)
            .background(color = MaterialTheme.colorScheme.onSurface)
    )
}