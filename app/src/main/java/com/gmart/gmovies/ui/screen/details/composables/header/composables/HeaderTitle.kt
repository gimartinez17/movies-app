package com.gmart.gmovies.ui.screen.details.composables.header.composables

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gmart.gmovies.utils.PreviewLayout
import com.gmart.gmovies.utils.ThemePreviews
import com.gmart.gmovies.utils.mockMovieDetails

@Composable
fun HeaderTitle(
    title: String,
    modifier: Modifier = Modifier,
) {
    Text(
        modifier = modifier.padding(bottom = 4.dp),
        text = title,
        color = MaterialTheme.colorScheme.onSurface,
        fontSize = 24.sp,
        lineHeight = 24.sp,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center,
        style = TextStyle(
            shadow = Shadow(
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.35f),
                offset = Offset(0f, 0f),
                blurRadius = 4f
            )
        ),
    )
}

@ThemePreviews
@Composable
private fun HeaderTitlePreview() {
    PreviewLayout {
        HeaderTitle(title = mockMovieDetails.title)
    }
}