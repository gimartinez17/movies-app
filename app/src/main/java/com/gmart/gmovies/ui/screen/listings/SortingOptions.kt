package com.gmart.gmovies.ui.screen.listings

import com.gmart.gmovies.R


internal val sortingOptions = listOf(
    "original_order.asc",
    "original_order.desc",
    "vote_average.asc",
    "vote_average.desc",
    "release_date.asc",
    "release_date.desc",
    "primary_release_date.asc",
    "primary_release_date.desc",
    "title.asc",
    "title.desc",
)

internal fun getSortNameByString(sortBy: String): Int {
    return when (sortBy) {
        "original_order.asc" -> R.string.original_order_asc
        "original_order.desc" -> R.string.original_order_desc
        "vote_average.asc" -> R.string.vote_average_asc
        "vote_average.desc" -> R.string.vote_average_desc
        "release_date.asc" -> R.string.release_date_asc
        "release_date.desc" -> R.string.release_date_desc
        "primary_release_date.asc" -> R.string.primary_release_date_asc
        "primary_release_date.desc" -> R.string.primary_release_date_desc
        "title.asc" -> R.string.title_asc
        "title.desc" -> R.string.title_desc
        else -> R.string.original_order_asc
    }
}

internal fun getSortNameByInt(sortBy: Int?): String? {
    return  when (sortBy) {
        1 -> "original_order.asc"
        2 -> "original_order.desc"
        3 -> "vote_average.asc"
        4 -> "vote_average.desc"
        5 -> "primary_release_date.asc"
        6 -> "primary_release_date.desc"
        7 -> "title.asc"
        8 -> "title.desc"
        9 -> "release_date.asc"
        10 -> "release_date.desc"
        else -> null
    }
}