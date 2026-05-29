package `in`.koreatech.koin.domain.usecase.store

import `in`.koreatech.koin.domain.repository.StoreRepository
import javax.inject.Inject

class GetStoreEventCountUseCase @Inject constructor(
    private val storeRepository: StoreRepository
) {
    suspend operator fun invoke(): Result<Int> {
        return storeRepository.getStoreEventCount()
    }
}
