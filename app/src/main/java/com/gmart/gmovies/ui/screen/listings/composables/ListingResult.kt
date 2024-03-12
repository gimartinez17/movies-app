package com.gmart.gmovies.ui.screen.listings.composables

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.DismissValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gmart.domain.model.Detail
import com.gmart.domain.model.MediaType
import com.gmart.gmovies.R
import com.gmart.gmovies.utils.DeviceScreenConfiguration
import com.gmart.gmovies.utils.DeviceScreenConfiguration.DeviceScreenSize.ExtraSmall
import com.gmart.gmovies.utils.DeviceScreenConfiguration.DeviceScreenSize.Large
import com.gmart.gmovies.utils.DeviceScreenConfiguration.DeviceScreenSize.Medium
import com.gmart.gmovies.utils.DeviceScreenConfiguration.DeviceScreenSize.Small
import com.gmart.gmovies.utils.PreviewLayout
import com.gmart.gmovies.utils.ThemePreviews
import com.gmart.gmovies.utils.mockMovieDetails
import com.gmart.gmovies.utils.rememberScreenConfiguration

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListingResult(
    modifier: Modifier = Modifier,
    data: List<Detail> = listOf(),
    mediaType: MediaType? = null,
    onMediaClick: (Int, MediaType) -> Unit = { _, _ -> },
    onMediaDelete: (Int, MediaType) -> Unit = { _, _ -> },
    configuration: DeviceScreenConfiguration = rememberScreenConfiguration(),
) {
    val dismissThreshold = 0.5f
    var currentFraction by remember { mutableFloatStateOf(0f) }
    val items by remember(key1 = configuration.size) {
        mutableIntStateOf(
            when (configuration.size) {
                ExtraSmall, Small -> 1
                Medium, Large -> 2
            }
        )
    }
    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Fixed(items),
        contentPadding = PaddingValues(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        items(data.size) { index ->
            val currentItem by rememberUpdatedState(data[index])
            val dismissState = rememberDismissState(
                confirmValueChange = {
                    if (it == DismissValue.DismissedToEnd && currentFraction >= dismissThreshold && currentFraction <= 1.0f) {
                        val itemType = currentItem.mediaType ?: mediaType
                        itemType?.let { type -> onMediaDelete(currentItem.id, type) }
                        true
                    } else {
                        false
                    }
                }
            )
            if (dismissState.currentValue != DismissValue.Default) {
                LaunchedEffect(Unit) { dismissState.reset() }
            }

            SwipeToDismiss(
                state = dismissState,
                directions = setOf(DismissDirection.StartToEnd),
                background = {
                    val backgroundColor by animateColorAsState(
                        targetValue = when (dismissState.targetValue) {
                            DismissValue.DismissedToEnd -> Color.Red.copy(alpha = 0.8f)
                            else -> MaterialTheme.colorScheme.surface
                        },
                        label = "",
                    )

                    Box(
                        modifier = Modifier
                            .background(
                                color = backgroundColor,
                                shape = MaterialTheme.shapes.medium
                            )
                            .fillMaxSize()
                            .padding(start = 16.dp)
                    ) {
                        currentFraction = dismissState.progress
                        Column(
                            modifier = Modifier.fillMaxHeight(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Delete,
                                contentDescription = stringResource(id = R.string.delete),
                                tint = Color.White
                            )
                            Text("Delete", fontSize = 14.sp, color = Color.White)
                        }
                    }
                },
                dismissContent = {
                    ListingItem(
                        modifier = Modifier
                            .shadow(4.dp, shape = MaterialTheme.shapes.medium, clip = false),
                        title = currentItem.title,
                        backdropPath = currentItem.backdropPath,
                        score = currentItem.voteAverage,
                        onClick = {
                            val itemType = currentItem.mediaType ?: mediaType
                            itemType?.let { type ->
                                onMediaClick(currentItem.id, type)
                            }
                        },
                    )
                }
            )
        }
    }
}

@Composable
@ThemePreviews
fun ListingResultPreview() {
    PreviewLayout {
        ListingResult(data = List(5) { mockMovieDetails })
    }
}