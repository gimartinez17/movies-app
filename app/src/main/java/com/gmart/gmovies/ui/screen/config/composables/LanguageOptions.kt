package com.gmart.gmovies.ui.screen.config.composables

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Translate
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
import com.gmart.domain.model.Language
import com.gmart.gmovies.R
import com.gmart.gmovies.ui.composable.AppBottomSheet
import com.gmart.gmovies.ui.composable.AppOption
import com.gmart.gmovies.utils.upperString


@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun LanguageOptions(
    modifier: Modifier = Modifier,
    options: List<Language>,
    selectedOption: String,
    valueChanged: (String) -> Unit = {},
) {
    var showBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()

    AppOption(
        modifier = modifier,
        icon = Icons.Default.Translate,
        text = stringResource(id = R.string.language),
        value = if (selectedOption == "system") selectedOption.upperString()
        else options.find { it.code == selectedOption }?.nativeName ?: selectedOption,
        onClick = { showBottomSheet = true }
    )

    if (showBottomSheet)
        AppBottomSheet(
            onDismiss = { showBottomSheet = false },
            sheetState = sheetState,
            content = {
                LazyColumn(modifier = Modifier.padding(vertical = 16.dp)) {
                    items(options.size) { index ->
                        ConfigRadioButton(
                            text = options[index].nativeName!! + " (${options[index].country})",
                            isSelected = selectedOption == options[index].code,
                            onClick = {
                                valueChanged(options[index].code ?: "system")
                                showBottomSheet = false
                            }
                        )
                    }
                }
            }
        )
}