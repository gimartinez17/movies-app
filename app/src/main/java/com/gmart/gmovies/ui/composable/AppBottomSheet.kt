package com.gmart.gmovies.ui.composable

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.gmart.gmovies.utils.PreviewLayout
import com.gmart.gmovies.utils.ThemePreviews

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBottomSheet(
    onDismiss: () -> Unit = {},
    sheetState: SheetState,
    content: @Composable (ColumnScope.() -> Unit)
) {
    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
        sheetState = sheetState,
        dragHandle = { BottomSheetDefaults.DragHandle() },
        content = content,
        tonalElevation = 0.dp,
        windowInsets = WindowInsets(0, 0, 0, 0),
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@ThemePreviews
@Composable
private fun AppBottomSheetPreview() {
    PreviewLayout {
        AppBottomSheet(
            sheetState = SheetState(false, initialValue = SheetValue.Expanded),
            content = {
                Text(
                    modifier = Modifier.padding(16.dp),
                    text = "Bottom Sheet Content"
                )
            }
        )
    }
}
