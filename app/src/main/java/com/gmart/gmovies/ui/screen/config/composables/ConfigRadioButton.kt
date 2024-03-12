package com.gmart.gmovies.ui.screen.config.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.gmart.gmovies.utils.PreviewLayout
import com.gmart.gmovies.utils.ThemePreviews

@Composable
fun ConfigRadioButton(
    text: String,
    isSelected: Boolean,
    onClick: (() -> Unit)?,
) {
    Row(
        modifier = Modifier
            .padding(horizontal = 10.dp)
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick?.invoke() }
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = text,
            modifier = Modifier
                .padding(start = 10.dp, end = 20.dp)
                .weight(1F)
        )
        RadioButton(
            modifier = Modifier.padding(end = 10.dp),
            selected = isSelected,
            onClick = onClick,
        )
    }
}

@ThemePreviews
@Composable
private fun ConfigRadioButtonPreview() {
    PreviewLayout {
        ConfigRadioButton(
            text = "English",
            isSelected = true,
            onClick = null,
        )
    }
}