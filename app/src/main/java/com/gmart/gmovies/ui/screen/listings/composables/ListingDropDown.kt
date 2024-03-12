package com.gmart.gmovies.ui.screen.listings.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.LiveTv
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.PlaylistAdd
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.gmart.domain.model.ListType
import com.gmart.domain.model.UserList
import com.gmart.gmovies.R
import com.gmart.gmovies.utils.PreviewLayout
import com.gmart.gmovies.utils.ThemePreviews

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListingDropDown(
    modifier: Modifier = Modifier,
    listNames: List<UserList>,
    selectIndex: Int = 0,
    onListClick: (Int) -> Unit = {},
    onAddListClick: () -> Unit = {},
) {
    var isExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ExposedDropdownMenuBox(
            expanded = isExpanded,
            onExpandedChange = { isExpanded = !isExpanded }
        ) {
            TextField(
                readOnly = true,
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth(),
                shape = MaterialTheme.shapes.extraLarge,
                value = getTitle(listNames[selectIndex]),
                onValueChange = { },
                leadingIcon = { Icon(getIcon(listNames[selectIndex].id), "") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded)
                },
                colors = ExposedDropdownMenuDefaults.textFieldColors(
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(6.dp),
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(6.dp),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    )
            )
            MaterialTheme(shapes = MaterialTheme.shapes.copy(extraSmall = RoundedCornerShape(28.dp))) {
                DropdownMenu(
                    modifier = Modifier.exposedDropdownSize(),
                    expanded = isExpanded,
                    onDismissRequest = { isExpanded = false }
                ) {
                    DropdownMenuItem(
                        leadingIcon = { Icon(Icons.Default.PlaylistAdd, "") },
                        text = { Text(text = "Add List") },
                        onClick = {
                            isExpanded = false
                            onAddListClick()
                        }
                    )
                    Divider()
                    listNames.forEachIndexed { index, list ->
                        DropdownMenuItem(
                            leadingIcon = { Icon(getIcon(listNames[index].id), "") },
                            text = { Text(text = getTitle(list)) },
                            onClick = {
                                isExpanded = false
                                onListClick(index)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun getTitle(list: UserList): String {
    return when (list.listType) {
        ListType.FAVOURITE_MOVIES -> stringResource(id = R.string.favourite_movies)
        ListType.FAVOURITE_TV -> stringResource(id = R.string.favourite_tv_shows)
        else -> list.name
    }
}

@Composable
private fun getIcon(id: Int): ImageVector {
    return when (id) {
        0 -> Icons.Default.Movie
        1 -> Icons.Default.LiveTv
        else -> Icons.Default.List
    }
}

@ThemePreviews
@Composable
fun ListingDropDownPreview() {
    val list1 = UserList(name = "Favourite Movie", listType = ListType.FAVOURITE_MOVIES, id = 1)
    val list2 = UserList(name = "Favourite Tv Show", listType = ListType.FAVOURITE_TV, id = 1)
    val list3 = UserList(name = "My movie list", listType = ListType.USER, id = 2)
    var selectIndex by rememberSaveable { mutableIntStateOf(0) }
    PreviewLayout {
        ListingDropDown(
            listNames = listOf(list1, list2, list3),
            selectIndex = selectIndex,
            onListClick = { selectIndex = it }
        )
    }
}