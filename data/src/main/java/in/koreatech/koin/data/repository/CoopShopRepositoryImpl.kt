package `in`.koreatech.koin.data.repository

import `in`.koreatech.koin.data.mapper.toCoopShop
import `in`.koreatech.koin.data.mapper.toCoopShopEntity
import `in`.koreatech.koin.data.source.local.CoopShopLocalDataSource
import `in`.koreatech.koin.data.source.remote.CoopShopRemoteDataSource
import `in`.koreatech.koin.domain.model.coopshop.CoopShop
import `in`.koreatech.koin.domain.model.coopshop.CoopShopType
import `in`.koreatech.koin.domain.repository.CoopShopRepository
import `in`.koreatech.koin.domain.util.suspendRunCatching
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CoopShopRepositoryImpl @Inject constructor(
    private val coopShopLocalDataSource: CoopShopLocalDataSource,
    private val coopShopRemoteDataSource: CoopShopRemoteDataSource
) : CoopShopRepository {
    override fun getCoopShopAll(): Flow<List<CoopShop>> =
        coopShopLocalDataSource.observeLatestCoopShops().map { shops -> shops.map { it.toCoopShop() } }

    override fun getCoopShopById(id: Int): Flow<CoopShop?> =
        coopShopLocalDataSource.observeLatestCoopShopByCoopNameId(id).map { it?.toCoopShop() }

    override suspend fun sync(): Result<Unit> = suspendRunCatching {
        val response = coopShopRemoteDataSource.getCoopShopAll()
        val coopShopTypeIds = CoopShopType.entries.map { it.id }.toSet()
        val entities = response.coopShops.map { coopShop ->
            coopShop.toCoopShop(
                semester = coopShop.semester ?: response.semester,
                updatedAt = coopShop.updatedAt ?: response.updatedAt
            ).toCoopShopEntity(coopNameId = coopShop.id.takeIf { it in coopShopTypeIds })
        }
        coopShopLocalDataSource.saveCoopShops(response.semester, entities)
    }
}
