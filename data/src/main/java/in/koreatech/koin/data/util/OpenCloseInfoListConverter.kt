package `in`.koreatech.koin.data.util

import androidx.room.TypeConverter
import `in`.koreatech.koin.data.model.coopshop.CachedOpenCloseInfo
import `in`.koreatech.koin.domain.model.coopshop.OpenCloseInfo
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class OpenCloseInfoListConverter {
    @TypeConverter
    fun fromString(value: String): List<OpenCloseInfo> = Json.decodeFromString<List<CachedOpenCloseInfo>>(value).map { it.toOpenCloseInfo() }

    @TypeConverter
    fun toString(value: List<OpenCloseInfo>): String =
        Json.encodeToString(value.map(CachedOpenCloseInfo::from))
}
