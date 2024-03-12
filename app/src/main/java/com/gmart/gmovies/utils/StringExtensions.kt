package com.gmart.gmovies.utils

fun List<String>.joinWithComa(max: Int?): String {
    val maxItems = (max?.let { minOf(this.size, it) } ?: this.size) - 1
    val finalList = this.take(maxItems + 1)
    return finalList.joinToString { it }
}

fun <T> Iterable<T>.joinWithSlash(transform: ((T) -> CharSequence)? = null): String {
    return joinTo(StringBuilder(), separator = "/", transform = transform).toString()
}

fun String.upperString(): String {
    return this.substring(0, 1).uppercase() + this.substring(1).lowercase()
}