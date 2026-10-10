package `in`.koreatech.koin.domain.usecase.orderShop

import `in`.koreatech.koin.domain.model.ordershop.OrderMenuList
import `in`.koreatech.koin.domain.repository.OrderShopRepository
import `in`.koreatech.koin.domain.util.suspendRunCatching
import javax.inject.Inject

class GetOrderShopMenuUseCase @Inject constructor(
    private val orderShopRepository: OrderShopRepository
) {
    suspend operator fun invoke(orderableShopId: Int): Result<List<OrderMenuList>> {
        return suspendRunCatching {
            orderShopRepository.getOrderableShopMenus(orderableShopId)
        }
    }
}
