package com.example.news.utils

import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

fun formatNewsDate(isoString: String): String {
    return try {
        // Parse the ISO 8601 string
        val parsedDate = ZonedDateTime.parse(isoString)

        val formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy hh:mm a", Locale.getDefault())

        parsedDate.format(formatter)
    } catch (e: Exception) {
        isoString
    }
}