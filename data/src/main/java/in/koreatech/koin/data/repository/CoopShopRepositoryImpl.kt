package `in`.koreatech.koin.data.repository

import `in`.koreatech.koin.data.mapper.toCoopShop
import `in`.koreatech.koin.data.response.coopshop.CoopShopResponse
import `in`.koreatech.koin.data.source.remote.CoopShopRemoteDataSource
import `in`.koreatech.koin.domain.model.coopshop.CoopShop
import `in`.koreatech.koin.domain.repository.CoopShopRepository
import javax.inject.Inject

class CoopShopRepositoryImpl @Inject constructor(
    private val coopShopRemoteDataSource: CoopShopRemoteDataSource
): CoopShopRepository {
    override suspend fun getCoopShopAll(): List<CoopShop> {
        return coopShopRemoteDataSource.getCoopShopAll().map(CoopShopResponse::toCoopShop)
    }

    override suspend fun getCoopShopById(id: Int): CoopShop {
        return coopShopRemoteDataSource.getCoopShopById(id).toCoopShop()
    }

}