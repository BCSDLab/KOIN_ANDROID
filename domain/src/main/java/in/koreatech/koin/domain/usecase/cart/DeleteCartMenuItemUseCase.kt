package `in`.koreatech.koin.domain.usecase.cart

import `in`.koreatech.koin.domain.repository.CartRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class DeleteCartMenuItemUseCase @Inject constructor(
    private val cartRepository: CartRepository
) {
    suspend operator fun invoke(cartMenuItemId: Int): Flow<Unit> = cartRepository.deleteCartMenuItem(cartMenuItemId)
}
