package `in`.koreatech.koin.domain.usecase.store

import `in`.koreatech.koin.domain.repository.StoreRepository
import javax.inject.Inject

class ResetCartUseCase @Inject constructor(
    private val storeRepository: StoreRepository
) {
    suspend operator fun invoke(): Result<Unit> = storeRepository.resetCart()
}
