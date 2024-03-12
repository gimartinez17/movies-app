package com.gmart.gmovies.ui.screen.details.composables.body.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.gmart.domain.model.Cast
import com.gmart.gmovies.R
import com.gmart.gmovies.utils.DeviceScreenConfiguration
import com.gmart.gmovies.utils.DeviceScreenConfiguration.DeviceScreenSize.ExtraSmall
import com.gmart.gmovies.utils.DeviceScreenConfiguration.DeviceScreenSize.Large
import com.gmart.gmovies.utils.DeviceScreenConfiguration.DeviceScreenSize.Medium
import com.gmart.gmovies.utils.DeviceScreenConfiguration.DeviceScreenSize.Small
import com.gmart.gmovies.utils.PreviewLayout
import com.gmart.gmovies.utils.ThemePreviews
import com.gmart.gmovies.utils.mockCastData
import com.gmart.gmovies.utils.rememberScreenConfiguration


@Composable
internal fun CastDetails(
    modifier: Modifier = Modifier,
    cast: List<Cast>?,
    onCastItemClick: (Int) -> Unit = {},
    onViewAllClick: () -> Unit = {},
    configuration: DeviceScreenConfiguration = rememberScreenConfiguration(),
) {
    val maxCast by remember(key1 = configuration.size) {
        val max = when (configuration.size) {
            ExtraSmall,Small -> 9
            Medium -> 12
            Large -> 16
        }
        mutableIntStateOf(max)
    }

    if (cast?.isNotEmpty() == true) {
        Column {
            Row(
                modifier = modifier.padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = stringResource(id = R.string.details_cast),
                    style = MaterialTheme.typography.titleMedium
                )
                if (cast.size > maxCast)
                    TextButton(onClick = onViewAllClick) {
                        Text(text = stringResource(id = R.string.view_all))
                    }
            }
            LazyRow(contentPadding = PaddingValues(horizontal = 8.dp)) {
                items(cast.take(maxCast)) { item ->
                    CastItem(
                        item,
                        modifier = Modifier.padding(end = 6.dp),
                        onClick = onCastItemClick,
                    )
                }
            }
        }
    }
}

@ThemePreviews
@Composable
private fun Preview() {
    PreviewLayout {
        CastDetails(
            cast = List(6) { mockCastData },
            onViewAllClick = {}
        )
    }
}