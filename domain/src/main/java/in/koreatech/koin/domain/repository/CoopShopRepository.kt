package `in`.koreatech.koin.domain.repository

import `in`.koreatech.koin.domain.model.coopshop.CoopShop

interface CoopShopRepository {
    suspend fun getCoopShopAll(): List<CoopShop>
    suspend fun getCoopShopById(id: Int): CoopShop
}