package `in`.koreatech.koin.domain.usecase.orderShop

import `in`.koreatech.koin.domain.model.ordershop.OrderShopSummary
import `in`.koreatech.koin.domain.repository.OrderShopRepository
import `in`.koreatech.koin.domain.util.suspendRunCatching
import javax.inject.Inject

class GetOrderShopSummaryUseCase @Inject constructor(
    private val orderShopRepository: OrderShopRepository
) {
    suspend operator fun invoke(orderableShopId: Int): Result<OrderShopSummary> {
        return suspendRunCatching {
            orderShopRepository.getOrderableShopSummary(orderableShopId)
        }
    }
}
