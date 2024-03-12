package com.gmart.gmovies.ui.screen.details.composables.body.composables

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.content.Intent.CATEGORY_BROWSABLE
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.net.Uri
import android.os.Build
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun ProvidersWebView(
    path: String,
    onPageFinished: () -> Unit = {},
) {
    AndroidView(
        factory = { context ->
            WebView(context).apply {
                settings.javaScriptEnabled = true
                webViewClient = object : WebViewClient() {
                    @RequiresApi(Build.VERSION_CODES.R)
                    override fun shouldOverrideUrlLoading(
                        view: WebView?,
                        request: WebResourceRequest?
                    ): Boolean {
                        val url = request?.url.toString()
                        try {
                            val intent = Intent(ACTION_VIEW, Uri.parse(url)).apply {
                                addCategory(CATEGORY_BROWSABLE)
                                flags = FLAG_ACTIVITY_NEW_TASK
                            }
                            context.startActivity(intent)
                        } catch (e: ActivityNotFoundException) {
                            view?.loadUrl(url)
                        }
                        return true
                    }

                    override fun onPageFinished(view: WebView?, url: String?) {
                        super.onPageFinished(view, url)
                        onPageFinished()
                        view?.loadUrl("""javascript:(function() { function hideElementsByClass(className) { var elements = document.getElementsByClassName(className); for(var i = 0; i < elements.length; i++) { elements[i].style.display='none'; } } hideElementsByClass('content_wrapper'); hideElementsByClass('scroller_wrap shortcut_bar_wrapper is_fading'); hideElementsByClass('single_column movie header_large'); hideElementsByClass('k-widget k-dropdown country_dropdown tmdb_theme'); })()""".trimIndent())
                    }
                }
                loadUrl(path)
            }
        }
    )
}