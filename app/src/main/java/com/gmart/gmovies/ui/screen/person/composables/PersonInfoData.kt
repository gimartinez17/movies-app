package com.gmart.gmovies.ui.screen.person.composables

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun PersonInfoData(
    modifier: Modifier = Modifier,
    title: String,
    value: String?,
) {
    if (value?.isNotBlank() == true) {
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
                text = value,
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}