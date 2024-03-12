package com.gmart.gmovies.ui.screen.details.composables.body.composables

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.gmart.gmovies.R
import com.gmart.gmovies.utils.joinWithComa


@Composable
internal fun CreatorDetails(
    modifier: Modifier = Modifier,
    creator: List<String>?
) {
    if (creator?.isNotEmpty() == true) {
        Row(modifier = modifier) {
            Text(
                modifier = Modifier.align(Alignment.Top),
                text = if (creator.size > 1) stringResource(id = R.string.details_creators)
                else stringResource(id = R.string.details_creator),
                style = MaterialTheme.typography.titleSmall,
            )
            Text(
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.Top),
                text = creator.joinWithComa(4),
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}