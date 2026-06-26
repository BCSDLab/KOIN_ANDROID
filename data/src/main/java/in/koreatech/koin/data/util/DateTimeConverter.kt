package `in`.koreatech.koin.data.util

import androidx.room.TypeConverter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class DateTimeConverter {
    @TypeConverter
    fun toLocalDateTime(localDateTime: String?): LocalDateTime? {
        return localDateTime?.let { LocalDateTime.parse(it, DateTimeFormatter.ISO_LOCAL_DATE_TIME) }
    }

    @TypeConverter
    fun fromLocalDateTime(localDateTime: LocalDateTime?): String? {
        return localDateTime?.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
    }
}
