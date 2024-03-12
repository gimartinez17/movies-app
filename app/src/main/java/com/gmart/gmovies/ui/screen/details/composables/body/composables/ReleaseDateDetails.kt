package com.gmart.gmovies.ui.screen.details.composables.body.composables

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.gmart.gmovies.R
import com.gmart.gmovies.utils.getSimpleFormatDate

@Composable
fun ReleaseDateDetails(
    modifier: Modifier = Modifier,
    visibility: Boolean,
    releaseDate: String?
) {
    if (releaseDate != null && visibility) {
        val context = LocalContext.current
        Row(modifier = modifier) {
            Text(
                modifier = Modifier.align(Alignment.Top),
                text = stringResource(id = R.string.details_release_date),
                style = MaterialTheme.typography.titleSmall,
            )
            Text(
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.Top),
                text = releaseDate.getSimpleFormatDate(context) ?: "",
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}