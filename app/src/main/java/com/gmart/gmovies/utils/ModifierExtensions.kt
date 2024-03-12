package com.gmart.gmovies.utils

import android.annotation.SuppressLint
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush

fun Modifier.conditional(
    condition: Boolean,
    isTrue: Modifier.() -> Modifier,
    isFalse: (Modifier.() -> Modifier)? = null
): Modifier {
    return if (condition) {
        then(isTrue(Modifier))
    } else if (isFalse != null) {
        then(isFalse(Modifier))
    } else {
        this
    }
}

@SuppressLint("UnnecessaryComposedModifier")
fun Modifier.shimmerLoadingAnimation(
    widthOfShadowBrush: Int = 350,
    angleOfAxisY: Float = 270f,
    durationMillis: Int = 1000,
): Modifier {
    return composed {
        val color = MaterialTheme.colorScheme.onSurface
        val colors = listOf(
            color.copy(alpha = 0.05f),
            color.copy(alpha = 0.15f),
            color.copy(alpha = 0.25f),
            color.copy(alpha = 0.15f),
            color.copy(alpha = 0.05f),
        )
        val transition = rememberInfiniteTransition(label = "")

        val translateAnim = transition.animateFloat(
            initialValue = 0f,
            targetValue = (durationMillis + widthOfShadowBrush).toFloat(),
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = durationMillis,
                    easing = FastOutSlowInEasing,
                ),
                repeatMode = RepeatMode.Restart,
            ),
            label = "Shimmer Loading",
        )

        this.background(
            brush = Brush.linearGradient(
                colors = colors,
                start = Offset(x = translateAnim.value - widthOfShadowBrush, y = 0.0f),
                end = Offset(x = translateAnim.value, y = angleOfAxisY),
            ),
        )
    }
}
