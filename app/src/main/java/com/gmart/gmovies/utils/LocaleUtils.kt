package com.gmart.gmovies.utils

import android.app.Activity
import android.app.LocaleManager
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.os.Build
import android.os.LocaleList
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat

fun localeSelection(
    context: Context,
    localeTag: String,
    country: String,
    restart: Boolean = false
) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        context.getSystemService(LocaleManager::class.java).applicationLocales =
            LocaleList.forLanguageTags("$localeTag-$country")
    } else {
        AppCompatDelegate.setApplicationLocales(
            LocaleListCompat.forLanguageTags("$localeTag-$country")
        )
    }
    if (restart) context.findActivity()?.recreateActivity()
}

fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}

fun Activity.recreateActivity() {
    finish()
    startActivity(Intent(this.intent))
}