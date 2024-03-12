package com.gmart.gmovies.ui.screen.listings.composables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.gmart.gmovies.R
import com.gmart.gmovies.ui.screen.listings.getSortNameByString
import com.gmart.gmovies.ui.screen.listings.sortingOptions
import com.gmart.gmovies.utils.PreviewLayout
import com.gmart.gmovies.utils.ThemePreviews

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateListContent(
    name: String,
    onNameChange: (String) -> Unit,
    description: String,
    onDescriptionChange: (String) -> Unit,
    isPublic: Boolean = false,
    onPublicChange: (Boolean) -> Unit,
    sortBy: String,
    onSortByChange: (String) -> Unit,
) {
    var isExpanded by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.large,
            value = name,
            onValueChange = onNameChange,
            placeholder = { Text(stringResource(id = R.string.name)) },
        )
        OutlinedTextField(
            modifier = Modifier
                .padding(top = 8.dp)
                .fillMaxWidth(),
            shape = MaterialTheme.shapes.large,
            value = description,
            minLines = 3,
            maxLines = 5,
            onValueChange = onDescriptionChange,
            placeholder = { Text(stringResource(id = R.string.description)) },
        )
        Row(
            modifier = Modifier.padding(top = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                stringResource(id = R.string.is_public_list),
                modifier = Modifier.weight(1f)
            )
            Switch(checked = isPublic, onCheckedChange = onPublicChange)
        }

        Text(
            stringResource(id = R.string.sort_by),
            modifier = Modifier.padding(top = 6.dp, bottom = 4.dp),
        )
        ExposedDropdownMenuBox(
            expanded = isExpanded,
            onExpandedChange = { isExpanded = !isExpanded }
        ) {
            Surface(
                shape = MaterialTheme.shapes.large,
                border = BorderStroke(1.dp, Color(red = 147, green = 143, blue = 153)),
            ) {
                TextField(
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .menuAnchor()
                        .fillMaxWidth(),
                    readOnly = true,
                    value = stringResource(id = getSortNameByString(sortBy)),
                    shape = MaterialTheme.shapes.large,
                    onValueChange = onSortByChange,
                    placeholder = { Text(stringResource(id = R.string.sort_by)) },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded)
                    },
                    colors = ExposedDropdownMenuDefaults.textFieldColors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                    )
                )
            }
            MaterialTheme(shapes = MaterialTheme.shapes.copy(extraSmall = RoundedCornerShape(28.dp))) {
                DropdownMenu(
                    modifier = Modifier.exposedDropdownSize(),
                    expanded = isExpanded,
                    onDismissRequest = { isExpanded = false }
                ) {
                    sortingOptions.forEach { value ->
                        DropdownMenuItem(
                            text = { Text(stringResource(id = getSortNameByString(value))) },
                            onClick = {
                                isExpanded = false
                                onSortByChange(value)
                            }
                        )
                    }
                }
            }
        }
    }
}

@ThemePreviews
@Composable
private fun CreateListContentPreview() {
    PreviewLayout {
        CreateListContent(
            name = "",
            onNameChange = {},
            description = "",
            onDescriptionChange = {},
            isPublic = false,
            onPublicChange = {},
            sortBy = sortingOptions.first(),
            onSortByChange = {}
        )
    }
}