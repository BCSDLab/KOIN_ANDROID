package `in`.koreatech.koin.data.repository

import `in`.koreatech.koin.data.mapper.toCoopShop
import `in`.koreatech.koin.data.response.coopshop.CoopShopResponse
import `in`.koreatech.koin.data.source.remote.CoopShopRemoteDataSource
import `in`.koreatech.koin.data.util.mapHttpFailure
import `in`.koreatech.koin.domain.error.coopshop.KoinCoopShopException
import `in`.koreatech.koin.domain.model.coopshop.CoopShop
import `in`.koreatech.koin.domain.repository.CoopShopRepository
import `in`.koreatech.koin.domain.util.suspendRunCatching
import javax.inject.Inject

class CoopShopRepositoryImpl @Inject constructor(
    private val coopShopRemoteDataSource: CoopShopRemoteDataSource
) : CoopShopRepository {
    override suspend fun getCoopShopAll(): Result<List<CoopShop>> {
        return suspendRunCatching {
            coopShopRemoteDataSource.getCoopShopAll().map(CoopShopResponse::toCoopShop)
        }.mapHttpFailure { }
    }

    override suspend fun getCoopShopById(id: Int): Result<CoopShop> {
        return suspendRunCatching {
            coopShopRemoteDataSource.getCoopShopById(id).toCoopShop()
        }.mapHttpFailure {
            on(404) throws KoinCoopShopException.CoopShopNotFoundException()
        }
    }
}
