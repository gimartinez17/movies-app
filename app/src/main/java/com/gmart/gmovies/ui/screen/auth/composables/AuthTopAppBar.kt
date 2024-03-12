package com.gmart.gmovies.ui.screen.auth.composables

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.gmart.gmovies.ui.theme.WebViewTopAppBarColor

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun AuthTopAppBar(url: String, onBackClick: () -> Unit) {
    TopAppBar(
        modifier = Modifier.zIndex(zIndex = 1f),
        title = {
            Text(
                text = url,
                modifier = Modifier.padding(8.dp),
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                color = Color.White,
                fontSize = 14.sp
            )
        },
        navigationIcon = {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier
                    .padding(8.dp)
                    .size(36.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = null,
                    tint = Color.White
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = WebViewTopAppBarColor,
        ),
    )
}