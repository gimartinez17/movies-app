package com.gmart.gmovies.ui.screen.details.composables.body.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.gmart.domain.model.WatchProviders
import com.gmart.gmovies.R
import com.gmart.gmovies.ui.composable.AppBottomSheet
import com.gmart.gmovies.ui.composable.Loading
import com.gmart.gmovies.utils.DeviceScreenConfiguration
import com.gmart.gmovies.utils.DeviceScreenConfiguration.DeviceScreenSize.ExtraSmall
import com.gmart.gmovies.utils.DeviceScreenConfiguration.DeviceScreenSize.Large
import com.gmart.gmovies.utils.DeviceScreenConfiguration.DeviceScreenSize.Medium
import com.gmart.gmovies.utils.DeviceScreenConfiguration.DeviceScreenSize.Small
import com.gmart.gmovies.utils.PreviewLayout
import com.gmart.gmovies.utils.ThemePreviews
import com.gmart.gmovies.utils.mockWatchProviders
import com.gmart.gmovies.utils.rememberScreenConfiguration
import com.gmart.gmovies.utils.upperString


@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ProvidersDetails(
    modifier: Modifier = Modifier,
    watchProviders: WatchProviders?,
    configuration: DeviceScreenConfiguration = rememberScreenConfiguration(),
) {
    var showLoading by remember { mutableStateOf(false) }
    var showProviders by remember { mutableStateOf(false) }
    var providersPath by remember { mutableStateOf("") }
    val modalBottomSheetState = rememberModalBottomSheetState()
    val density = LocalDensity.current
    val screenWidth = LocalConfiguration.current.screenWidthDp
    val providerSize by remember {
        val size = when (configuration.size) {
            ExtraSmall, Small -> 36.dp
            Medium, Large -> 46.dp
        }
        mutableStateOf(size)
    }

    if (watchProviders?.info?.isNotEmpty() == true) {
        var width by remember { mutableStateOf(0.dp) }
        val itemsCount by remember(key1 = width) {
            val n = (screenWidth - width.value.toInt() - 32) / (providerSize.value.toInt() + 10)
            mutableIntStateOf(if (watchProviders.info!!.size > n) n - 1 else n)
        }

        val sortedProviders = watchProviders.info!!.sortedBy { it.displayPriority }.take(itemsCount)
        Column(modifier = Modifier.padding(bottom = 8.dp)) {
            Text(
                text = stringResource(id = R.string.details_providers),
                modifier = modifier.padding(horizontal = 16.dp),
                style = MaterialTheme.typography.titleMedium
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                LazyRow(
                    modifier = Modifier.padding(top = 8.dp, bottom = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    items(sortedProviders.size) { index ->
                        ProviderIcon(
                            logoPath = sortedProviders[index].logoPath ?: "",
                            itemSize = providerSize
                        )
                    }
                    if (watchProviders.info!!.size > sortedProviders.size) {
                        item {
                            MoreProviders(
                                providersSize = watchProviders.info!!.size,
                                sortedProvidersSize = sortedProviders.size,
                                itemSize = providerSize
                            )
                        }
                    }
                    item {
                        ViewMoreButton(
                            modifier = Modifier
                                .onSizeChanged {
                                    if (width == 0.dp) width = with(density) { it.width.toDp() }
                                }
                                .height(providerSize)
                                .wrapContentWidth(),
                            onClick = {
                                showLoading = true
                                showProviders = true
                                providersPath = watchProviders.link ?: ""
                            },
                            showLoading = showLoading
                        )
                    }
                }
            }
        }
        if (showProviders) AppBottomSheet(
            onDismiss = { showProviders = false },
            sheetState = modalBottomSheetState,
            content = {
                Box(
                    modifier = Modifier
                        .wrapContentHeight()
                        .verticalScroll(rememberScrollState())
                ) {
                    ProvidersWebView(
                        path = providersPath,
                        onPageFinished = { showLoading = false }
                    )
                }
            }
        )
    }
}

@Composable
private fun MoreProviders(
    providersSize: Int,
    sortedProvidersSize: Int,
    itemSize: Dp,
) {
    Card(
        shape = RoundedCornerShape(8.0.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .width(itemSize)
            .height(itemSize)
    ) {
        Box(Modifier.fillMaxSize()) {
            Text(
                "+${providersSize - sortedProvidersSize}",
                modifier = Modifier.align(Alignment.Center),
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
            )
        }
    }
}

@Composable
private fun ViewMoreButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    showLoading: Boolean
) {
    Button(
        modifier = modifier,
        onClick = onClick,
        contentPadding = PaddingValues(horizontal = 12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(1.dp),
        ),
    ) {
        if (showLoading) Loading(modifier = Modifier.size(14.dp), strokeWidth = 2.dp)
        else Icon(
            modifier = Modifier.height(14.dp),
            painter = painterResource(R.drawable.just_watch_short_logo),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
        )
        Spacer(modifier = Modifier.padding(horizontal = 4.dp))
        Text(
            text = stringResource(id = R.string.watch_now).uppercase(),
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@ThemePreviews
@Composable
private fun ProvidersDetailsPreview() {
    PreviewLayout {
        ProvidersDetails(watchProviders = mockWatchProviders)
    }
}