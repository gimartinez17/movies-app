package com.gmart.gmovies.ui.screen.details.composables.header.composables

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun HeaderInformation(modifier: Modifier = Modifier, info: List<String?>) {
    val textStyle =
        MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurface)
    Row(modifier = modifier) {
        info.filterNotNull().forEachIndexed { index, text ->
            if (index != 0) DetailsDot(modifier = Modifier.align(Alignment.CenterVertically))
            Text(text = text, style = textStyle)
        }
    }
}
