package `in`.koreatech.koin.domain.usecase.store

import `in`.koreatech.koin.domain.model.store.CartAdd
import `in`.koreatech.koin.domain.repository.StoreRepository
import javax.inject.Inject

class AddCartItemUseCase @Inject constructor(
    private val storeRepository: StoreRepository
) {
    suspend operator fun invoke(cartAdd: CartAdd): Result<Unit> {
        return storeRepository.addCartItem(cartAdd)
    }
}
