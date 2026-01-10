package com.example.news.utils

import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

fun formatNewsDate(isoString: String): String {
    return try {
        // 1. Parse the ISO 8601 string
        val parsedDate = ZonedDateTime.parse(isoString)

        // 2. Define your desired format
        val formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy hh:mm a", Locale.getDefault())

        // 3. Format the date
        parsedDate.format(formatter)
    } catch (e: Exception) {
        isoString // Return original if parsing fails
    }
}