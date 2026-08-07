package org.elnix.aura.base.utils

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.DateTimeFormat
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.toLocalDateTime
import org.elnix.aura.base.model.DateTimeFormats
import org.elnix.aura.base.utils.DateUtils.defaultDateTimeFormatter
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import kotlin.time.Clock
import kotlin.time.Instant

public object DateUtils {
    /**
     * Default date time formatter
     * Outputs `MMM dd, yyyy HH:mm:ss`
     */
    private val defaultDateTimeFormatter = LocalDateTime.Format {
        monthName(MonthNames.ENGLISH_ABBREVIATED)
        chars(" ")
        day()
        chars(", ")
        year()
        chars(" ")
        hour()
        chars(":")
        minute()
        chars(":")
        second()
    }

    /**
     * Format a timestamp (milliseconds) to a readable datetime string.
     * Used by logs and backup tabs to format file dates.
     *
     * @return [String] formatted as [defaultDateTimeFormatter]
     */
    public fun Long.formatDateTime(format: DateTimeFormat<LocalDateTime> = defaultDateTimeFormatter): String {
        val instant = Instant.fromEpochMilliseconds(this)
        val localDateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
        return localDateTime.format(format)
    }

    /**
     * Format the current moment as a datetime string.
     *
     * @param format the datetime format to apply
     * @return [String] the current datetime formatted according to the specified format
     */
    public fun nowFormattedDateTime(format: DateTimeFormat<LocalDateTime> = defaultDateTimeFormatter): String {
        val instant = Clock.System.now()
        val localDateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
        return localDateTime.format(format)
    }

    /**
     * Format the current time as a time string.
     *
     * @param format the time format to apply (default: 24-hour with seconds)
     * @return [String] the current time formatted according to the specified format
     */
    public fun nowFormattedTime(format: DateTimeFormat<kotlinx.datetime.LocalTime> = DateTimeFormats.time24HourSeconds): String {
        val instant = Clock.System.now()
        val localDateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
        return localDateTime.time.format(format)
    }

    /**
     * Format the current date as a date string.
     *
     * @param format the date format to apply (default: European format)
     * @return [String] the current date formatted according to the specified format
     */
    public fun nowFormattedDate(format: DateTimeFormat<kotlinx.datetime.LocalDate> = DateTimeFormats.dateEu): String {
        val instant = Clock.System.now()
        val localDateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
        return localDateTime.date.format(format)
    }

    /**
     * Format duration
     * Takes a timestamp and format it into a duration in hours, minutes and seconds
     * Depending on the duration, the minutes and hours may or may not be displayed (e.g. if under 60 min, no hours)
     *
     * @return [String] the formatted duration
     */
    public fun Long.formatDuration(): String {
        return when {
            this >= 60 -> {
                val hours = this / 60
                val mins = this % 60
                if (mins > 0) "${hours}h ${mins}m" else "${hours}h"
            }

            else -> "$this min"
        }
    }

    public fun isValidTimeFormat(formatter: String): Boolean = try {
        val timeFormatter = DateTimeFormatter.ofPattern(formatter)
        val now = LocalTime.now()
        now.format(timeFormatter)
        true
    } catch (e: Exception) {
        println("❌ Time format validation failed: '$formatter' -> ${e.message}")
        false
    }

    public fun isValidDateFormat(formatter: String): Boolean = try {
        val dateFormatter = DateTimeFormatter.ofPattern(formatter)
        val today = LocalDate.now()
        today.format(dateFormatter)
        true
    } catch (e: Exception) {
        println("❌ Date format validation failed: '$formatter' -> ${e.message}")
        false
    }



//fun Long.timeAgo(): String {
//    val seconds = (System.currentTimeMillis() - this) / 1000
//    return when {
//        seconds < 60 -> "${seconds}s ago"
//        seconds < 3600 -> "${seconds / 60}m ago"
//        seconds < 86400 -> "${seconds / 3600}h ago"
//        seconds < 2592000 -> "${seconds / 86400}d ago"
//        else -> "${seconds / 2592000}mo ago"
//    }
//}

}