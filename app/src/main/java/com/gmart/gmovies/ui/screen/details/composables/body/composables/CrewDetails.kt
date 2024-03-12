package com.gmart.gmovies.ui.screen.details.composables.body.composables

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.gmart.domain.model.Crew
import com.gmart.gmovies.utils.joinWithComa


@Composable
internal fun CrewDetails(
    modifier: Modifier = Modifier,
    title: String,
    crew: List<Crew>?,
) {
    if (crew?.isNotEmpty() == true) {
        Row(modifier = modifier) {
            Text(
                modifier = Modifier.align(Alignment.Top),
                text = title,
                style = MaterialTheme.typography.titleSmall,
            )
            Text(
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.Top),
                text = crew.map { it.name }.joinWithComa(4),
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}