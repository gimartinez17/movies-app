package com.gmart.gmovies.ui.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.gmart.gmovies.R

@Composable
fun PoweredBy(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = "Powered by",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodySmall,
        )
        Image(
            modifier = Modifier
                .padding(8.dp)
                .height(18.dp),
            painter = painterResource(R.drawable.tmdb_square_logo),
            contentDescription = "",
        )
        Image(
            modifier = Modifier
                .padding(8.dp)
                .height(18.dp),
            painter = painterResource(R.drawable.just_watch_short_logo),
            contentDescription = "",
        )
    }
}