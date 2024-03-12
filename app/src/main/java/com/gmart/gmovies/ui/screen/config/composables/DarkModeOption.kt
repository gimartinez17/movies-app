package com.gmart.gmovies.ui.screen.config.composables

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
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
import com.gmart.gmovies.R
import com.gmart.gmovies.ui.composable.AppBottomSheet
import com.gmart.gmovies.ui.composable.AppOption
import com.gmart.gmovies.utils.upperString


enum class DarkModeOption(val label: Int, val value: String) {
    ON(R.string.dark_mode_on, "on"),
    OFF(R.string.dark_mode_off, "off"),
    SYSTEM(R.string.dark_mode_system_setting, "system");

    companion object {
        fun fromValue(value: String?): DarkModeOption = DarkModeOption.values()
            .find { it.value == value } ?: SYSTEM
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun DarkModeOptions(
    modifier: Modifier = Modifier,
    options: List<DarkModeOption>,
    selectedOption: DarkModeOption,
    valueChanged: (DarkModeOption) -> Unit = {},
) {
    var showOptions by remember { mutableStateOf(false) }
    val modalBottomSheetState = rememberModalBottomSheetState()

    AppOption(
        modifier = modifier,
        icon = Icons.Default.DarkMode,
        text = stringResource(id = R.string.dark_mode),
        value = selectedOption.value.upperString(),
        onClick = { showOptions = true }
    )

    if (showOptions) AppBottomSheet(
        onDismiss = { showOptions = false },
        sheetState = modalBottomSheetState,
        content = {
            LazyColumn(modifier = Modifier.padding(vertical = 16.dp)) {
                items(options.size) { index ->
                    ConfigRadioButton(
                        text = stringResource(id = options[index].label),
                        isSelected = selectedOption == options[index],
                        onClick = {
                            valueChanged(options[index])
                            showOptions = false
                        }
                    )
                }
            }
        }
    )
}