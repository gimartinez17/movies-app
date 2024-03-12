package com.gmart.gmovies.ui.screen.details.composables.header.composables

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.dp
import com.gmart.gmovies.ui.composable.HorizontalSpacer
import com.gmart.gmovies.ui.theme.DarkGold
import com.gmart.gmovies.ui.theme.LightGold
import com.gmart.gmovies.ui.theme.PearlWhite
import com.gmart.gmovies.ui.theme.SilverLight
import com.gmart.gmovies.ui.theme.isDarkMode
import com.gmart.gmovies.utils.PreviewLayout
import com.gmart.gmovies.utils.ThemePreviews

@Composable
fun Score(
    modifier: Modifier = Modifier,
    score: Float,
    showTextScore: Boolean = true,
) {
    val isDarkTheme = MaterialTheme.colorScheme.isDarkMode()
    val starScore = score / 2f
    val fullStars = starScore.toInt()
    val remainingStars = 5 - fullStars - 1
    val partialStar = starScore - fullStars
    val startSize = 16.dp
    val tintColor = if (isDarkTheme) PearlWhite else SilverLight
    val starColor = if (isDarkTheme) DarkGold else LightGold

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        repeat(fullStars) {
            Icon(
                Icons.Filled.Star,
                contentDescription = null,
                modifier = Modifier.size(startSize),
                tint = starColor
            )
        }
        if (partialStar > 0) {
            Icon(
                Icons.Filled.Star,
                modifier = Modifier
                    .drawWithContent {
                        with(drawContext.canvas.nativeCanvas) {
                            val checkPoint = saveLayer(null, null)
                            drawContent()
                            drawRect(
                                starColor,
                                topLeft = Offset(0f, 0f),
                                size = Size(size.width * partialStar, size.height),
                                blendMode = BlendMode.SrcIn
                            )
                            restoreToCount(checkPoint)
                        }
                    }
                    .size(startSize),
                contentDescription = null,
                tint = tintColor,
            )
        }
        repeat(remainingStars) {
            Icon(
                Icons.Filled.Star,
                modifier = Modifier.size(startSize),
                tint = tintColor,
                contentDescription = null
            )
        }
        if (showTextScore) {
            HorizontalSpacer(8.dp)
            Text(
                text = "%.1f".format(score),
                color = starColor,
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}

@ThemePreviews
@Composable
private fun ScorePreview() {
    PreviewLayout {
        Score(score = 7f)
    }
}