package com.gmart.gmovies.ui.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.gmart.gmovies.R
import com.gmart.gmovies.utils.PreviewLayout
import com.gmart.gmovies.utils.ThemePreviews

@Composable
fun AppError(
    modifier: Modifier = Modifier,
    onRetry: () -> Unit = {},
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            modifier = Modifier,
            painter = painterResource(R.drawable.error),
            alpha = 0.75F,
            contentDescription = "",
        )
        Text(
            stringResource(id = R.string.error_message),
            modifier = Modifier.padding(top = 32.dp, start = 32.dp, end = 32.dp, bottom = 16.dp),
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )
        OutlinedButton(onClick = onRetry) {
            Text(stringResource(id = R.string.retry))
        }
    }
}

@Composable
@ThemePreviews
private fun AppErrorPreview() {
    PreviewLayout {
        AppError()
    }
}