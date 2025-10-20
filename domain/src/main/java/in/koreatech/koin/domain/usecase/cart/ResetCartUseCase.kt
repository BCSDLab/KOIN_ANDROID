package `in`.koreatech.koin.domain.usecase.cart

import `in`.koreatech.koin.domain.repository.CartRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class ResetCartUseCase @Inject constructor(
    private val cartRepository: CartRepository
) {
    suspend operator fun invoke(): Flow<Unit> = cartRepository.resetCart()
}
