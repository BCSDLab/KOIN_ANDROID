package `in`.koreatech.koin.domain.usecase.store

import `in`.koreatech.koin.domain.model.store.CartItemsCount
import `in`.koreatech.koin.domain.repository.StoreRepository
import javax.inject.Inject

class GetCartItemsCountUseCase @Inject constructor(
    private val storeRepository: StoreRepository
) {
    suspend operator fun invoke(): Result<CartItemsCount> = storeRepository.getCartItemsCount()
}
