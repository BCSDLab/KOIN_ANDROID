package `in`.koreatech.koin.domain.usecase.store

import `in`.koreatech.koin.domain.model.store.OrderInProgress
import `in`.koreatech.koin.domain.repository.StoreRepository
import javax.inject.Inject

class GetOrderInProgressUseCase @Inject constructor(
    private val storeRepository: StoreRepository
) {
    suspend operator fun invoke(): Result<List<OrderInProgress>> = storeRepository.getOrderInProgress()
}
