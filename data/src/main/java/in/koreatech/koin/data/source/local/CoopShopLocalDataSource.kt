package `in`.koreatech.koin.data.source.local

import `in`.koreatech.koin.data.dao.CoopShopDao
import `in`.koreatech.koin.data.entity.CoopShopEntity
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class CoopShopLocalDataSource @Inject constructor(
    private val coopShopDao: CoopShopDao
) {
    suspend fun getLatestCoopShops(): List<CoopShopEntity> = coopShopDao.getLatestCoopShops()

    fun observeLatestCoopShops(): Flow<List<CoopShopEntity>> = coopShopDao.observeLatestCoopShops()

    suspend fun getLatestCoopShopByCoopNameId(coopNameId: Int): CoopShopEntity? =
        coopShopDao.getLatestCoopShopByCoopNameId(coopNameId)

    fun observeLatestCoopShopByCoopNameId(coopNameId: Int): Flow<CoopShopEntity?> =
        coopShopDao.observeLatestCoopShopByCoopNameId(coopNameId)

    suspend fun saveCoopShops(semester: String, coopShops: List<CoopShopEntity>) {
        coopShopDao.replaceCoopShops(semester, coopShops)
    }

    suspend fun saveCoopShop(coopShop: CoopShopEntity) {
        coopShopDao.insertCoopShop(coopShop)
    }
}
