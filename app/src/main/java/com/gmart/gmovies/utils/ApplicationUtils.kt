package com.gmart.gmovies.utils

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log


enum class ApplicationPackage(
    val appName: String = "",
    val packageName: String,
    val appUrl: String,
    val webUrl: String
) {
    FACEBOOK(
        packageName = "com.facebook.katana",
        appUrl = "fb://page/",
        webUrl = "https://www.facebook.com/"
    ),
    INSTAGRAM(
        packageName = "com.instagram.android",
        appUrl = "http://instagram.com/_u/",
        webUrl = "http://instagram.com/"
    ),
    TWITTER(
        packageName = "com.twitter.android",
        appUrl = "twitter://user?user_id=",
        webUrl = "https://twitter.com/"
    ),
    TIKTOK(
        packageName = "com.ss.android.ugc.trill",
        appUrl = "http://vm.tiktok.com/",
        webUrl = "https://www.tiktok.com/@"
    ),
    YOUTUBE(
        packageName = "com.google.android.youtube",
        appUrl = "https://www.youtube.com/channel/",
        webUrl = "https://www.youtube.com/channel/"
    ),
    YOUTUBE_VIDEOS(
        packageName = "com.google.android.youtube",
        appUrl = "https://www.youtube.com/channel/",
        webUrl = "https://www.youtube.com/channel/"
    ),
}


fun Context.openApplication(id: String, app: ApplicationPackage) {
    when {
        this.isApplicationInstalled(app.packageName) -> {
            val sendIntent = Intent().apply {
                action = Intent.ACTION_VIEW
                data = Uri.parse(app.appUrl + id)
                `package` = app.packageName
            }
            try {
                this.startActivity(sendIntent)
            } catch (ex: ActivityNotFoundException) {
                Log.d("openApplication", "${app.name} is not installed")
            }
        }

        else -> {
            val intent = Intent().apply {
                action = Intent.ACTION_VIEW
                data = Uri.parse(app.webUrl + id)
            }
            this.startActivity(intent)
        }
    }
}

fun Context.isApplicationInstalled(packageName: String): Boolean {
    return try {
        packageManager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES)
        true
    } catch (e: PackageManager.NameNotFoundException) {
        false
    }
}