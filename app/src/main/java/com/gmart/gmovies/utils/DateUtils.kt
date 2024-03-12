package com.gmart.gmovies.utils

import android.content.Context
import android.util.Log
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

fun String.getSimpleFormatDate(context: Context): String? {
    val locale = context.resources.configuration.locales.get(0)
    val sdf = SimpleDateFormat("yyyy-MM-dd", locale)
    val format = SimpleDateFormat("MMMM dd, yyyy", locale)
    return try {
        val date = sdf.parse(this)
        format.format(date)
            .replaceFirstChar { if (it.isLowerCase()) it.titlecase(locale) else it.toString() }
    } catch (e: Exception) {
        Log.e("getSimpleFormatDate: ", e.toString())
        null
    }
}

fun String.toDate(): Date? {
    val locale = Locale.getDefault()
    val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", locale)
    return try {
        sdf.parse(this)
    } catch (e: Exception) {
        Log.e("toDate: ", e.toString())
        null
    }
}

fun String.getYear(context: Context): String? {
    val locale = context.resources.configuration.locales.get(0)
    var sdf = SimpleDateFormat("yyyy-MM-dd", locale)
    return try {
        val date = sdf.parse(this)
        sdf = SimpleDateFormat("yyyy", locale)
        val year = date?.let { sdf.format(it) }
        year
    } catch (e: Exception) {
        Log.e("getYear: ", e.toString())
        null
    }
}

fun calculateAge(context: Context, birthdate: String, deathDate: String? = null): Int {
    val birthYear = birthdate.getYear(context)?.toInt()
    val deathYear = deathDate?.getYear(context)?.toInt()
    val currentYear = Calendar.getInstance().get(Calendar.YEAR)

    return when {
        birthYear != null && deathYear != null -> deathYear - birthYear
        birthYear != null -> currentYear - birthYear
        else -> 0
    }
}

fun Int?.getHoursAndMinutes(): String? {
    if (this == null) return null
    val hours = this / 60
    val remainingMinutes = this % 60
    val hoursStr = if (hours > 0) "${hours}h " else ""
    val minutesString = if (remainingMinutes > 0) "${remainingMinutes}m" else ""
    return hoursStr + minutesString
}