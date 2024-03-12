package com.gmart.gmovies.ui.screen.details.composables.body.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.gmart.domain.model.Provider
import com.gmart.gmovies.BuildConfig
import com.gmart.gmovies.utils.AppDimensions
import com.gmart.gmovies.utils.PreviewLayout
import com.gmart.gmovies.utils.ThemePreviews
import com.gmart.gmovies.utils.conditional
import com.gmart.gmovies.utils.joinWithSlash
import com.gmart.gmovies.utils.mockWatchProviders

@Composable
fun ProviderItem(
    modifier: Modifier = Modifier,
    provider: Provider?,
    shape: Shape = MaterialTheme.shapes.medium,
    onClick: ((String) -> Unit)? = null,
) {
    if (provider?.logoPath?.isNotEmpty() == true) {
        Card(
            shape = shape,
            elevation = CardDefaults.cardElevation(defaultElevation = AppDimensions.cardElevation),
            modifier = modifier.conditional(
                condition = onClick != null && provider.logoPath?.isNotEmpty() == true,
                { clickable { onClick!!.invoke(provider.name ?: "") } }
            ),
        ) {
            Row(modifier = Modifier.padding(10.dp)) {
                Card(
                    shape = RoundedCornerShape(8.0.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    //colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier.padding(end = 10.dp)
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(BuildConfig.LOGO_URL + provider.logoPath)
                            .build(),
                        contentDescription = null,
                        contentScale = ContentScale.FillHeight,
                        modifier = Modifier
                            .width(36.dp)
                            .height(36.dp)
                    )
                }
                Column {
                    Text(
                        provider.name ?: "",
                        style = MaterialTheme.typography.labelMedium,
                    )
                    Text(
                        provider.from?.toList()?.joinWithSlash { it.type } ?: "",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontSize = 10.sp
                        ),
                    )
                }
            }
        }
    }
}

@ThemePreviews
@Composable
private fun ProviderItemPreview() {
    PreviewLayout {
        ProviderItem(provider = mockWatchProviders.info?.first())
    }
}