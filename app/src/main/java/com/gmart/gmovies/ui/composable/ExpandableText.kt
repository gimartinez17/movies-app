package com.gmart.gmovies.ui.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntSize
import com.gmart.gmovies.R
import com.gmart.gmovies.utils.PreviewLayout
import com.gmart.gmovies.utils.ThemePreviews

@Composable
fun ExpandableText(
    modifier: Modifier = Modifier,
    text: String,
    minimizedMaxLines: Int = 1,
) {
    var cutText by remember(text) { mutableStateOf<String?>(null) }
    var expanded by remember { mutableStateOf(false) }
    var textLayoutResult by remember { mutableStateOf<TextLayoutResult?>(null) }
    var seeMoreSize by remember { mutableStateOf<IntSize?>(null) }
    var seeMoreOffset by remember { mutableStateOf<Offset?>(null) }

    LaunchedEffect(text, expanded, textLayoutResult, seeMoreSize) {
        val lastLineIndex = minimizedMaxLines - 1
        if (!expanded && textLayoutResult != null && seeMoreSize != null
            && lastLineIndex + 1 == textLayoutResult?.lineCount
            && textLayoutResult?.isLineEllipsized(lastLineIndex) == true
        ) {
            var lastCharIndex = textLayoutResult!!.getLineEnd(lastLineIndex, visibleEnd = true) + 1
            var charRect: Rect
            do {
                lastCharIndex -= 1
                charRect = textLayoutResult!!.getCursorRect(lastCharIndex)
            } while (
                charRect.left > (textLayoutResult!!.size.width - seeMoreSize!!.width)
            )
            seeMoreOffset = Offset(charRect.left, charRect.bottom - seeMoreSize!!.height)
            cutText = text.substring(startIndex = 0, endIndex = lastCharIndex)
        }
    }

    Box(modifier) {
        Text(
            text = cutText ?: text,
            maxLines = if (expanded) Int.MAX_VALUE else minimizedMaxLines,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Justify,
            onTextLayout = { textLayoutResult = it },
        )
        if (!expanded) {
            val density = LocalDensity.current
            Text(
                stringResource(id = R.string.see_more),
                onTextLayout = { seeMoreSize = it.size },
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .then(
                        if (seeMoreOffset != null)
                            Modifier.offset(
                                x = with(density) { seeMoreOffset!!.x.toDp() },
                                y = with(density) { seeMoreOffset!!.y.toDp() },
                            )
                        else
                            Modifier
                    )
                    .clickable {
                        expanded = true
                        cutText = null
                    }
                    .alpha(if (seeMoreOffset != null) 1f else 0f)
            )
        }
    }
}

@ThemePreviews
@Composable
private fun ExpandableTextPreview() {
    PreviewLayout {
        ExpandableText(
            text = List(10) { "Lorem ipsum dolor sit amet, consectetur adipiscing elit. " }
                .joinToString(separator = ""),
            minimizedMaxLines = 3,
        )
    }
}