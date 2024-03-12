package com.gmart.gmovies.ui.screen.details.composables.header.composables

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.util.lerp

@Composable
fun CollapsingHeaderLayout(
    collapseFraction: () -> Float,
    minHeaderHeight: Int,
    maxHeaderHeight: Int,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Layout(
        modifier = modifier,
        content = content
    ) { measures, constraints ->
        val headerHeight = lerp(maxHeaderHeight, minHeaderHeight, collapseFraction())
        val placeable =
            measures.first().measure(Constraints.fixed(constraints.maxWidth, headerHeight))

        layout(
            width = constraints.maxWidth,
            height = headerHeight
        ) {
            placeable.placeRelative(0, 0)
        }
    }
}