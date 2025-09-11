package `in`.koreatech.koin.domain.usecase.store

import `in`.koreatech.koin.domain.model.store.OrderOnGoingRelated
import `in`.koreatech.koin.domain.repository.StoreRepository
import javax.inject.Inject

class GetOnGoingRelatedUseCase @Inject constructor(
    private val storeRepository: StoreRepository
) {
    suspend operator fun invoke(): List<OrderOnGoingRelated> = storeRepository.getOrderOnGoings()
}
