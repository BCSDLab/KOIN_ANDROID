package `in`.koreatech.koin.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import `in`.koreatech.koin.data.constant.DBConstant

@Entity(tableName = DBConstant.CACHE_METADATA)
data class CacheMetadataEntity(
    @PrimaryKey val cacheKey: String,
    @ColumnInfo(name = "updated_time") val updatedTime: Long
)
