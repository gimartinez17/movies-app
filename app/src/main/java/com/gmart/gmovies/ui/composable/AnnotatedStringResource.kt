package com.gmart.gmovies.ui.composable

import android.text.Annotation
import android.text.SpannedString
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.core.text.getSpans

@Composable
fun annotatedStringResource(
    @StringRes id: Int,
    spanStyles: (Annotation) -> SpanStyle? = { null }
): AnnotatedString {
    val resources = LocalContext.current.resources
    val spannedString = SpannedString(resources.getText(id))
    val resultBuilder = AnnotatedString.Builder()
    resultBuilder.append(spannedString.toString())
    spannedString.getSpans<Annotation>(0, spannedString.length).forEach { annotation ->
        val spanStart = spannedString.getSpanStart(annotation)
        val spanEnd = spannedString.getSpanEnd(annotation)
        resultBuilder.addStringAnnotation(
            tag = annotation.key,
            annotation = annotation.value,
            start = spanStart,
            end = spanEnd
        )
        spanStyles(annotation)?.let { resultBuilder.addStyle(it, spanStart, spanEnd) }
    }
    return resultBuilder.toAnnotatedString()
}
