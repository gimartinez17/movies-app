package com.gmart.gmovies.ui.screen.config.composables

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Language
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.gmart.domain.model.Country
import com.gmart.gmovies.R
import com.gmart.gmovies.ui.composable.AppBottomSheet
import com.gmart.gmovies.ui.composable.AppOption


@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun CountryOptions(
    modifier: Modifier = Modifier,
    countries: List<Country>,
    selectedOption: String,
    valueChanged: (String) -> Unit = {},
) {
    var showOptions by remember { mutableStateOf(false) }
    val modalBottomSheetState = rememberModalBottomSheetState()

    AppOption(
        modifier = modifier,
        icon = Icons.Default.Language,
        text = stringResource(id = R.string.country),
        value = countries.find { it.country == selectedOption }?.nativeName ?: selectedOption,
        onClick = { showOptions = true }
    )

    if (showOptions)
        AppBottomSheet(
            onDismiss = { showOptions = false },
            sheetState = modalBottomSheetState,
            content = {
                LazyColumn(modifier = Modifier.padding(vertical = 16.dp)) {
                    items(countries.size) { index ->
                        ConfigRadioButton(
                            text = countries[index].nativeName!!,
                            isSelected = selectedOption == countries[index].country,
                            onClick = {
                                valueChanged(countries[index].country ?: "system")
                                showOptions = false
                            }
                        )
                    }
                }
            }
        )
}