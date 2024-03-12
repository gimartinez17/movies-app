package com.gmart.gmovies.ui.screen.config.composables

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.gmart.gmovies.R
import com.gmart.gmovies.ui.composable.AppBottomSheet
import com.gmart.gmovies.ui.composable.AppOption
import com.gmart.gmovies.ui.composable.annotatedStringResource
import com.gmart.gmovies.ui.theme.isDarkMode

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutOption(modifier: Modifier = Modifier) {
    var showAboutInfo by remember { mutableStateOf(false) }
    val modalBottomSheetState = rememberModalBottomSheetState()

    AppOption(
        modifier = modifier,
        icon = Icons.Default.Info,
        text = stringResource(id = R.string.about),
        onClick = { showAboutInfo = true }
    )

    if (showAboutInfo) AppBottomSheet(
        onDismiss = { showAboutInfo = false },
        sheetState = modalBottomSheetState,
        content = {
            AboutContent()
        }
    )
}

@Composable
private fun AboutContent() {
    val context = LocalContext.current

    val tmdbAnnotatedString = annotatedStringResource(
        id = R.string.tmdb_attribution_message,
        spanStyles = { annotation ->
            when (annotation.key) {
                "url" -> SpanStyle(textDecoration = TextDecoration.Underline)
                else -> null
            }
        }
    )
    val justWatchAnnotatedString = annotatedStringResource(
        id = R.string.just_watch_attribution_message,
        spanStyles = { annotation ->
            when (annotation.key) {
                "url" -> SpanStyle(textDecoration = TextDecoration.Underline)
                else -> null
            }
        }
    )

    Column {
        Image(
            modifier = Modifier
                .width(150.dp)
                .align(Alignment.CenterHorizontally),
            painter = if (MaterialTheme.colorScheme.isDarkMode())
                painterResource(R.drawable.app_logo_night)
            else
                painterResource(R.drawable.app_logo_light),
            contentDescription = "",
            contentScale = ContentScale.FillWidth,
        )
        Text(
            modifier = Modifier
                .padding(top = 4.dp, start = 16.dp, end = 16.dp, bottom = 16.dp)
                .align(Alignment.CenterHorizontally),
            text = "Version: ${getAppVersion(context)}",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodySmall,
        )
        Divider()
        Text(
            modifier = Modifier
                .padding(top = 32.dp, start = 16.dp, end = 16.dp),
            text = stringResource(id = R.string.app_description),
            style = MaterialTheme.typography.bodySmall,
        )
        ClickableText(
            text = tmdbAnnotatedString,
            modifier = Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp),
            style = MaterialTheme.typography.bodySmall,
            onClick = {offset ->
                tmdbAnnotatedString.getStringAnnotations(
                    tag = "url",
                    start = offset,
                    end = offset
                )
                    .firstOrNull()
                    ?.let { onOpenUrl(context, it.item) }
            }
        )
        ClickableText(
            text = justWatchAnnotatedString,
            modifier = Modifier.padding(
                top = 16.dp,
                start = 16.dp,
                end = 16.dp,
                bottom = 32.dp
            ),
            style = MaterialTheme.typography.bodySmall,
            onClick = { offset ->
                justWatchAnnotatedString.getStringAnnotations(
                    tag = "url",
                    start = offset,
                    end = offset
                )
                    .firstOrNull()
                    ?.let { onOpenUrl(context, it.item) }
            }
        )
        Divider()
        Text(
            modifier = Modifier
                .padding(top = 16.dp, start = 16.dp, end = 16.dp, bottom = 32.dp)
                .align(Alignment.CenterHorizontally),
            text = stringResource(id = R.string.created_by) + "Gimena Martinez",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodySmall,
        )
        // Rate Us button
    }
}

fun onOpenUrl(context: Context, url: String) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    context.startActivity(intent)
}

fun getAppVersion(context: Context): String {
    return try {
        val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
        packageInfo.versionName
    } catch (e: PackageManager.NameNotFoundException) {
        "Unknown"
    }
}
