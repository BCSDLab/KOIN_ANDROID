package `in`.koreatech.koin.data.source.local

import `in`.koreatech.koin.data.dao.CacheMetadataDao
import `in`.koreatech.koin.data.entity.CacheMetadataEntity
import javax.inject.Inject

class CacheLocalDataSource @Inject constructor(
    val cacheMetadataDao: CacheMetadataDao
) {
    suspend fun getCachedTime(cacheKey: String): Long? {
        return cacheMetadataDao.getUpdatedTime(cacheKey)
    }

    suspend fun updateCachedTime(cacheKey: String) {
        cacheMetadataDao.insert(
            CacheMetadataEntity(
                cacheKey = cacheKey,
                updatedTime = System.currentTimeMillis()
            )
        )
    }
}