package com.gmart.gmovies.ui.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.gmart.gmovies.utils.PreviewLayout
import com.gmart.gmovies.utils.ThemePreviews

@Composable
fun AppOption(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    text: String,
    value: String? = null,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(color = MaterialTheme.colorScheme.primary),
                onClick = onClick
            )
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceColorAtElevation(16.dp))
                .padding(6.dp)
        ) {
            Icon(imageVector = icon, contentDescription = "")
        }
        Text(
            text = text,
            modifier = Modifier
                .weight(1f)
                .padding(start = 12.dp)
        )
        if (value != null) Text(
            text = value,
            modifier = Modifier.padding(horizontal = 12.dp),
            style = MaterialTheme.typography.bodyMedium,
        )
        Icon(imageVector = Icons.Filled.ArrowForward, contentDescription = "")
    }
}

@ThemePreviews
@Composable
private fun AppOptionPreview() {
    PreviewLayout {
        AppOption(
            icon = Icons.Default.Settings,
            text = "Settings",
            value = "Value",
            onClick = {}
        )
    }
}
