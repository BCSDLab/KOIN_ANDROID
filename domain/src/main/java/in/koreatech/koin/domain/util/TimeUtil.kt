package `in`.koreatech.koin.domain.util

import java.time.DayOfWeek
import java.time.Duration
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.time.temporal.ChronoUnit
import java.util.Date
import java.util.Locale

object TimeUtil {
    private const val DATE_PARSE_PATTERN = "yyyy-M-d"
    private const val DATE_TIME_PARSE_PATTERN = "yyyy-M-d H:mm"
    private const val DATE_FORMAT_PATTERN = "yyyy-MM-dd"
    private const val COMPACT_DATE_PATTERN = "yyMMdd"
    private const val DATE_WITH_DAY_PATTERN = "MM/dd (EE)"
    private const val KOREAN_DATE_PATTERN = "M월 d일"

    private val dateParser by lazy { DateTimeFormatter.ofPattern(DATE_PARSE_PATTERN, Locale.getDefault()) }
    private val dateTimeParser by lazy { DateTimeFormatter.ofPattern(DATE_TIME_PARSE_PATTERN, Locale.getDefault()) }
    private val dateFormatter by lazy { DateTimeFormatter.ofPattern(DATE_FORMAT_PATTERN, Locale.getDefault()) }
    private val compactDateFormatter by lazy { DateTimeFormatter.ofPattern(COMPACT_DATE_PATTERN, Locale.getDefault()) }
    private val dateWithDayFormatter by lazy { DateTimeFormatter.ofPattern(DATE_WITH_DAY_PATTERN, Locale.getDefault()) }
    private val koreanDateFormatter by lazy { DateTimeFormatter.ofPattern(KOREAN_DATE_PATTERN, Locale.getDefault()) }

    fun getCurrentTime(): Date {
        return Date()
    }

    fun isWeekend(dateString: String): Boolean {
        val dayOfWeek = parseDate(dateString).dayOfWeek
        return dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY
    }

    fun getNextDayDate(date: Date): Date {
        return date.toLocalDate().plusDays(1).toStartOfDayDate()
    }

    fun getPreviousDayDate(date: Date): Date {
        return date.toLocalDate().minusDays(1).toStartOfDayDate()
    }

    fun dateFormatToYYYYMMDD(date: Date): String {
        return date.toLocalDate().format(dateFormatter)
    }

    fun dateFormatToYYMMDD(date: Date): String {
        return date.toLocalDate().format(compactDateFormatter)
    }

    fun dateFormatToMMDDEE(date: Date): String {
        return date.toLocalDate().format(dateWithDayFormatter)
    }

    fun stringToDateYYYYMMDD(date: String): Date {
        return try {
            parseDate(date).toStartOfDayDate()
        } catch (_: DateTimeParseException) {
            stringToDateYYMMDD(date)
        }
    }

    fun stringToDateYYMMDD(date: String): Date {
        return LocalDate.parse(date, compactDateFormatter).toStartOfDayDate()
    }

    fun compareWithCurrentTime(time: String): Long { // HH:mm
        val now = LocalDateTime.now()
        val compareDateTime = LocalDateTime.parse("${now.toLocalDate().format(dateFormatter)} $time", dateTimeParser)
        return Duration.between(now, compareDateTime).toMillis()
    }

    fun formatDateToKorean(dateString: String): String {
        return parseDate(dateString).format(koreanDateFormatter)
    }

    fun getDateDifferenceInDays(
        date1: Date,
        date2: Date
    ): Int {
        return ChronoUnit.DAYS.between(date2.toLocalDate(), date1.toLocalDate()).toInt()
    }

    fun isToday(dateString: String): Boolean {
        return parseDate(dateString) == LocalDate.now()
    }

    fun isTomorrow(dateString: String): Boolean {
        return parseDate(dateString) == LocalDate.now().plusDays(1)
    }

    private fun parseDate(date: String): LocalDate {
        val dateOnly = date.substringBefore('T').substringBefore(' ')
        return LocalDate.parse(dateOnly, dateParser)
    }

    private fun Date.toLocalDate(): LocalDate {
        return Instant.ofEpochMilli(time).atZone(ZoneId.systemDefault()).toLocalDate()
    }

    private fun LocalDate.toStartOfDayDate(): Date {
        return Date.from(atStartOfDay(ZoneId.systemDefault()).toInstant())
    }
}
