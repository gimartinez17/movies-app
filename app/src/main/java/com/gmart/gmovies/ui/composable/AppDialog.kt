package com.gmart.gmovies.ui.composable

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.gmart.gmovies.utils.PreviewLayout
import com.gmart.gmovies.utils.ThemePreviews

@Composable
fun AppDialog(
    title: String,
    body: String,
    dismissText: String,
    confirmText: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    dismissOnBackPress: Boolean = true,
) {
    AlertDialog(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
        title = { Text(text = title) },
        text = { Text(text = body) },
        onDismissRequest = { onDismiss() },
        containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(2.dp),
        confirmButton = {
            TextButton(onClick = { onConfirm() }) {
                Text(confirmText)
            }
        },
        dismissButton = {
            TextButton(onClick = { onDismiss() }) {
                Text(dismissText)
            }
        },
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnBackPress = dismissOnBackPress
        )
    )
}

@Composable
fun AppDialog(
    title: String,
    body: @Composable () -> Unit,
    dismissText: String,
    confirmText: String,
    neutralText: String? = null,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit = {},
    onNeutral: () -> Unit = {},
    dismissOnClickOutside: Boolean = true,
) {
    Dialog(
        onDismissRequest = { onDismiss() },
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnClickOutside = dismissOnClickOutside
        )
    ) {
        Box(
            modifier = Modifier.sizeIn(minWidth = 280.dp),
            propagateMinConstraints = true
        ) {
            Surface(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
                shape = AlertDialogDefaults.shape,
                color = AlertDialogDefaults.containerColor,
                tonalElevation = 2.0.dp,
            ) {
                Column(modifier = Modifier.padding(PaddingValues(all = 24.dp))) {
                    CompositionLocalProvider(LocalContentColor provides AlertDialogDefaults.titleContentColor) {
                        val textStyle = MaterialTheme.typography.headlineSmall
                        ProvideTextStyle(textStyle) {
                            Box(
                                Modifier
                                    .padding(PaddingValues(bottom = 16.dp))
                                    .align(Alignment.Start)
                            ) {
                                Text(text = title)
                            }
                        }
                    }
                    CompositionLocalProvider(LocalContentColor provides AlertDialogDefaults.textContentColor) {
                        val textStyle =
                            MaterialTheme.typography.bodyMedium
                        ProvideTextStyle(textStyle) {
                            Box(
                                Modifier
                                    .weight(weight = 1f, fill = false)
                                    .padding(PaddingValues(bottom = 24.dp))
                                    .align(Alignment.Start)
                            ) {
                                body()
                            }
                        }
                    }

                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.CenterHorizontally),
                        onClick = { onConfirm() }
                    ) {
                        Text(confirmText)
                    }
                    neutralText?.let {
                        OutlinedButton(
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.CenterHorizontally),
                            onClick = { onNeutral() }
                        ) {
                            Text(neutralText)
                        }
                    }
                    TextButton(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.CenterHorizontally),
                        onClick = { onDismiss() }
                    ) {
                        Text(dismissText)
                    }
                }
            }
        }
    }
}

@ThemePreviews
@Composable
fun AppDialogPreview() {
    PreviewLayout {
        Row {
            AppDialog(
                title = "Title",
                body = "Body",
                dismissText = "Dismiss",
                confirmText = "Confirm",
                onDismiss = {},
                onConfirm = {}
            )
        }
    }
}

@ThemePreviews
@Composable
fun AppDialog3Preview() {
    PreviewLayout {
        Row {
            AppDialog(
                title = "Title",
                body = { Text(text = "Body") },
                dismissText = "Dismiss",
                confirmText = "Confirm",
                neutralText = "Neutral",
                onDismiss = {},
                onConfirm = {},
                onNeutral = {}
            )
        }
    }
}
