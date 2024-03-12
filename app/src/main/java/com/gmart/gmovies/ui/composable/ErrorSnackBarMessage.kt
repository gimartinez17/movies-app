package com.gmart.gmovies.ui.composable


import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.res.stringResource
import com.gmart.gmovies.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun ErrorSnackBarMessage(
    scaffoldState: SnackbarHostState,
    message: String,
    scope: CoroutineScope,
    onActionPerform: (() -> Unit)? = null
) {
    val retry = stringResource(id = R.string.retry)
    LaunchedEffect(Unit) {
        scope.launch {
            val result = if (onActionPerform != null) scaffoldState.showSnackbar(
                message = message,
                actionLabel = retry,
                duration = SnackbarDuration.Indefinite
            ) else scaffoldState.showSnackbar(
                message = message,
                duration = SnackbarDuration.Short
            )
            when (result) {
                SnackbarResult.ActionPerformed -> onActionPerform?.invoke()
                SnackbarResult.Dismissed -> scaffoldState.currentSnackbarData?.dismiss()
            }
        }
    }
}
