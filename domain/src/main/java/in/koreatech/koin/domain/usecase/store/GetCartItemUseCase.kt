package `in`.koreatech.koin.domain.usecase.store

import `in`.koreatech.koin.domain.model.store.Cart
import `in`.koreatech.koin.domain.repository.StoreRepository
import javax.inject.Inject

class GetCartItemUseCase @Inject constructor(
    private val storeRepository: StoreRepository
) {
    suspend operator fun invoke(): Result<Cart> = storeRepository.getCartItems()
}
