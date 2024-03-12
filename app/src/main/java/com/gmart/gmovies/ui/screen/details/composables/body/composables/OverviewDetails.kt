package com.gmart.gmovies.ui.screen.details.composables.body.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.gmart.gmovies.ui.composable.ExpandableText


@Composable
fun OverviewDetails(
    modifier: Modifier = Modifier,
    title: String,
    overview: String?,
) {
    if (overview?.isNotEmpty() == true) {
        Column(modifier = modifier) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
            )
            ExpandableText(
                modifier = Modifier.padding(top = 4.dp),
                text = overview,
                minimizedMaxLines = 4
            )
        }
    }
}