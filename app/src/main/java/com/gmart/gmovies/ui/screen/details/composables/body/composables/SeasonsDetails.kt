package com.gmart.gmovies.ui.screen.details.composables.body.composables

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.gmart.gmovies.R


@Composable
internal fun SeasonsDetails(
    modifier: Modifier = Modifier,
    seasons: Int?
) {
    if (seasons != null) {
        Row(modifier = modifier) {
            Text(
                modifier = Modifier.align(Alignment.Top),
                text = (if (seasons > 1) stringResource(id = R.string.details_seasons)
                else stringResource(id = R.string.details_season)) + ": ",
                style = MaterialTheme.typography.titleSmall,
            )
            Text(
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.Top),
                text = seasons.toString() + " season${if (seasons > 1) "s" else ""}",
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}