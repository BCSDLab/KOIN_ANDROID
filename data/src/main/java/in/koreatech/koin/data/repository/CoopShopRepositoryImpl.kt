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
        coopShopLocalDataSource.saveCoopShops(
            semester = response.semester,
            coopShops = response.coopShops.map { it.toCoopShop(response.semester, response.updatedAt).toCoopShopEntity() }
        )
        CoopShopType.entries.forEach { type ->
            val coopShop = coopShopRemoteDataSource.getCoopShopById(type.id)
            coopShopLocalDataSource.saveCoopShop(
                coopShop.toCoopShop(
                    semester = requireNotNull(coopShop.semester),
                    updatedAt = requireNotNull(coopShop.updatedAt)
                ).toCoopShopEntity(coopNameId = type.id)
            )
        }

    }
}
