package com.gmart.gmovies.ui.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.gmart.gmovies.utils.PreviewLayout
import com.gmart.gmovies.utils.ThemePreviews

@Composable
fun FilledTabRow(
    tabs: List<String>,
    modifier: Modifier = Modifier,
    roundedCorner: CornerBasedShape = CircleShape,
    height: Dp = Dp.Unspecified,
    backgroundColor: Color = MaterialTheme.colorScheme.surfaceColorAtElevation(6.dp),
    indicatorColor: Color = MaterialTheme.colorScheme.primary
) {
    var selectedIndex by remember { mutableIntStateOf(0) }

    TabRow(
        selectedTabIndex = selectedIndex,
        contentColor = backgroundColor,
        containerColor = backgroundColor,
        modifier = modifier
            .clip(roundedCorner)
            .fillMaxWidth(),
        indicator = { Box {} },
        divider = {}
    ) {
        tabs.forEachIndexed { index, text ->
            val selected = selectedIndex == index
            Tab(
                modifier = if (selected) Modifier
                    .padding(4.dp)
                    .height(height)
                    .clip(roundedCorner)
                    .background(indicatorColor)
                else Modifier
                    .clip(roundedCorner)
                    .height(height)
                    .background(backgroundColor),
                selected = selected,
                onClick = { selectedIndex = index },
                text = {
                    Text(
                        text = text,
                        color = if (!selected) MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7F)
                        else Color.Unspecified
                    )
                }
            )
        }
    }
}

@ThemePreviews
@Composable
private fun TabsPreview() {
    PreviewLayout {
        FilledTabRow(
            listOf("Movies", "Shows"),
            modifier = Modifier,
            height = 32.dp,
            roundedCorner = CircleShape
        )
    }
}