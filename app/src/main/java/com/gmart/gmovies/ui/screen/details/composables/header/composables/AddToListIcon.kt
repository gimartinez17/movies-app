package com.gmart.gmovies.ui.screen.details.composables.header.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlaylistAdd
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.gmart.domain.model.UserList

@Composable
internal fun AddToListIcon(
    modifier: Modifier = Modifier,
    lists: List<UserList> = emptyList(),
    onMenuItemClick: (Int) -> Unit = {},
    showContrastColor: Boolean,
) {
    var expanded by remember { mutableStateOf(false) }
    Box(modifier = modifier) {
        IconButton(
            onClick = { expanded = !expanded },
            modifier = Modifier
                .statusBarsPadding()
                .padding(vertical = 8.dp)
                .clip(CircleShape)
                .size(36.dp)
                .background(
                    color = if (showContrastColor)
                        MaterialTheme.colorScheme.surface.copy(alpha = 0.4f)
                    else
                        Color.Transparent,
                    shape = CircleShape
                )
        ) {
            Icon(
                imageVector = Icons.Default.PlaylistAdd,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface,
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            lists.forEach { list ->
                DropdownMenuItem(
                    text = { Text(list.name) },
                    onClick = {
                        expanded = false
                        onMenuItemClick(list.id)
                    }
                )
            }
        }
    }
}