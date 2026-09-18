package `in`.koreatech.koin.data.source.local

import `in`.koreatech.koin.data.dao.DiningDao
import `in`.koreatech.koin.data.entity.DiningEntity
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class DiningLocalDataSource @Inject constructor(
    private val diningDao: DiningDao
) {
    fun observeDining(cacheDate: String): Flow<List<DiningEntity>> = diningDao.observeDining(cacheDate)

    suspend fun saveDining(cacheDate: String, dining: List<DiningEntity>) = diningDao.replaceDining(cacheDate, dining)
}
