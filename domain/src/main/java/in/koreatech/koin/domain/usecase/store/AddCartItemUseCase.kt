package `in`.koreatech.koin.domain.usecase.store

import `in`.koreatech.koin.domain.model.store.CartAdd
import `in`.koreatech.koin.domain.repository.StoreRepository
import javax.inject.Inject

class AddCartItemUseCase @Inject constructor(
    private val storeRepository: StoreRepository
) {
    suspend operator fun invoke(cartAdd: CartAdd): Result<Unit> {
        if (cartAdd.quantity !in 0..11) {
            throw IllegalArgumentException("Quantity must be between 0 and 11")
        }

        return storeRepository.addCartItem(cartAdd)
    }
}
