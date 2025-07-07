package `in`.koreatech.koin.domain.usecase.cart

import `in`.koreatech.koin.domain.repository.CartRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ResetCartUseCase  @Inject constructor(
    private val cartRepository: CartRepository
){
    operator fun invoke(): Flow<Unit> = flow {
        emitAll(cartRepository.resetCart())
    }
}
