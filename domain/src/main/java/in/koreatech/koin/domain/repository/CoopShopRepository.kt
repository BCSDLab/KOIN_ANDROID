package `in`.koreatech.koin.domain.repository

import `in`.koreatech.koin.domain.model.coopshop.CoopShop

interface CoopShopRepository {
    suspend fun getCoopShopAll(): Result<List<CoopShop>>

    suspend fun getCoopShopById(id: Int): Result<CoopShop>
}
