package `in`.koreatech.koin.domain.usecase.orderShop

import `in`.koreatech.koin.domain.model.ordershop.OrderMenuList
import `in`.koreatech.koin.domain.model.ordershop.OrderShop
import `in`.koreatech.koin.domain.model.ordershop.OrderShopSummary
import `in`.koreatech.koin.domain.repository.OrderShopRepository
import javax.inject.Inject

class GetOrderShopMenuUseCase @Inject constructor(
    private val orderShopRepository: OrderShopRepository
) {
    suspend operator fun invoke(orderableShopId: Int): List<OrderMenuList> {
        return orderShopRepository.getOrderableShopMenus(orderableShopId)
    }
}
