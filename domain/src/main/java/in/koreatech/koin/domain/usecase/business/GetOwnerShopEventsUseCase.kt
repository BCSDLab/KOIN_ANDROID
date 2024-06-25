package `in`.koreatech.koin.domain.usecase.business

import `in`.koreatech.koin.domain.model.store.ShopEvents
import `in`.koreatech.koin.domain.repository.StoreRepository
import javax.inject.Inject

class GetOwnerShopEventsUseCase @Inject constructor(
    private val storeRepository: StoreRepository,
) {
    suspend operator fun invoke(shopId: Int): ShopEvents {
        return storeRepository.getOwnerShopEvents(shopId)
    }
}
