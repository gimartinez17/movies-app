package com.gmart.gmovies.ui.screen.search.composable

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ElevatedFilterChip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.gmart.domain.model.MediaType
import com.gmart.gmovies.R
import com.gmart.gmovies.utils.PreviewLayout
import com.gmart.gmovies.utils.ThemePreviews


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchFilterChips(selectedChip: MediaType, onChipSelected: (MediaType) -> Unit = {}) {
    Row {
        ElevatedFilterChip(
            selected = selectedChip == MediaType.MOVIE,
            onClick = { onChipSelected(MediaType.MOVIE) },
            label = { Text(text = stringResource(id = R.string.movies)) })
        Spacer(modifier = Modifier.padding(horizontal = 4.dp))
        ElevatedFilterChip(
            selected = selectedChip == MediaType.TV,
            onClick = { onChipSelected(MediaType.TV) },
            label = { Text(text = stringResource(id = R.string.tv_shows)) })
    }
}

@ThemePreviews
@Composable
private fun SearchScreenPreview() {
    PreviewLayout {
        SearchFilterChips(MediaType.MOVIE) { }
    }
}