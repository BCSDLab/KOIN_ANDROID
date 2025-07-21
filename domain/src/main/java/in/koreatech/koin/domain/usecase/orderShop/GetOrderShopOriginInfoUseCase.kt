package `in`.koreatech.koin.domain.usecase.orderShop

import `in`.koreatech.koin.domain.model.ordershop.OrderShop
import `in`.koreatech.koin.domain.repository.OrderShopRepository
import javax.inject.Inject

class GetOrderShopOriginInfoUseCase @Inject constructor(
    private val orderShopRepository: OrderShopRepository
) {
    suspend operator fun invoke(orderableShopId: Int): OrderShop {
        return orderShopRepository.getOrderableShopDetail(orderableShopId)
    }
}
