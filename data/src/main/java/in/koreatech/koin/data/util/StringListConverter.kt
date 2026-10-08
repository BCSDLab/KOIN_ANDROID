package `in`.koreatech.koin.data.util

import androidx.room.TypeConverter
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class StringListConverter {
    @TypeConverter
    fun fromString(value: String): List<String> = Json.decodeFromString(value)

    @TypeConverter
    fun toString(value: List<String>): String = Json.encodeToString(value)
}
