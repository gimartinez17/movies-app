package com.gmart.gmovies.ui.screen.details.composables.body.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.gmart.domain.model.Cast
import com.gmart.gmovies.ui.composable.UserAvatar
import com.gmart.gmovies.utils.PreviewLayout
import com.gmart.gmovies.utils.ThemePreviews
import com.gmart.gmovies.utils.mockCastData

@Composable
fun CastItem(cast: Cast, modifier: Modifier = Modifier, onClick: (Int) -> Unit = {}) {
    Box(
        modifier = modifier
            .clip(MaterialTheme.shapes.medium)
            .clickable { onClick(cast.id) },
    ) {
        ConstraintLayout(
            modifier = Modifier
                .padding(vertical = 6.dp, horizontal = 4.dp)
                .width(100.dp)
        ) {
            val imageSize = 80.dp
            val (imageRef, nameRef, characterRef) = createRefs()
            UserAvatar(
                modifier = Modifier
                    .constrainAs(imageRef) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
                path = cast.profilePath,
                imageSize = imageSize,
            )
            Text(
                text = cast.name,
                style = MaterialTheme.typography.labelMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(top = 4.dp, bottom = 2.dp)
                    .constrainAs(nameRef) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        top.linkTo(imageRef.bottom)
                        bottom.linkTo(characterRef.top)
                        width = Dimension.fillToConstraints
                    }
            )
            Text(
                text = cast.character,
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .constrainAs(characterRef) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        top.linkTo(nameRef.bottom)
                        bottom.linkTo(parent.bottom)
                        width = Dimension.fillToConstraints
                    }
            )
        }
    }
}

@ThemePreviews
@Composable
private fun CastItemPreview() {
    PreviewLayout {
        CastItem(cast = mockCastData)
    }

}
