package `in`.koreatech.koin.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import `in`.koreatech.koin.data.constant.DBConstant
import `in`.koreatech.koin.data.entity.CacheMetadataEntity

@Dao
interface CacheMetadataDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(cacheMetadata: CacheMetadataEntity)

    @Query("SELECT updated_time from ${DBConstant.CACHE_METADATA} where cacheKey = :cacheKey LIMIT 1")
    suspend fun getUpdatedTime(cacheKey: String): Long?
}
