package `in`.koreatech.koin.domain.usecase.store

import `in`.koreatech.koin.domain.repository.StoreRepository
import javax.inject.Inject

class DeleteCartItemUseCase @Inject constructor(
    private val storeRepository: StoreRepository
) {
    suspend operator fun invoke(cartMenuItemId: Int): Result<Unit> = storeRepository.deleteCartItem(cartMenuItemId)
}
