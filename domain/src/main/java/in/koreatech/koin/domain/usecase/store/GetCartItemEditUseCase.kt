package `in`.koreatech.koin.domain.usecase.store

import `in`.koreatech.koin.domain.model.store.CartItemEdit
import `in`.koreatech.koin.domain.repository.StoreRepository
import javax.inject.Inject

class GetCartItemEditUseCase @Inject constructor(
    private val storeRepository: StoreRepository
) {
    suspend operator fun invoke(cartMenuItemId: Int): Result<CartItemEdit> = storeRepository.getCartItemEdit(cartMenuItemId)
}
