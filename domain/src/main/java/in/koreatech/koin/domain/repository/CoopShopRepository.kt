package `in`.koreatech.koin.domain.repository

import `in`.koreatech.koin.domain.model.coopshop.CoopShop
import kotlinx.coroutines.flow.Flow

interface CoopShopRepository {
    fun getCoopShopAll(): Flow<List<CoopShop>>

    fun getCoopShopById(id: Int): Flow<CoopShop?>

    suspend fun sync(): Result<Unit>
}
