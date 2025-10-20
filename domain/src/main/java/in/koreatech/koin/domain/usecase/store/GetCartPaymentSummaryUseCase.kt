package `in`.koreatech.koin.domain.usecase.store

import `in`.koreatech.koin.domain.model.store.CartPaymentSummary
import `in`.koreatech.koin.domain.repository.StoreRepository
import javax.inject.Inject

class GetCartPaymentSummaryUseCase @Inject constructor(
    private val storeRepository: StoreRepository
) {
    suspend operator fun invoke(type: String): Result<CartPaymentSummary> = storeRepository.getCartPaymentSummary(type)
}
