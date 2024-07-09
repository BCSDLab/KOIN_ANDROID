package `in`.koreatech.koin.domain.usecase.store

import `in`.koreatech.koin.domain.model.store.StoreEvent
import `in`.koreatech.koin.domain.repository.StoreRepository

import javax.inject.Inject

class GetStoreEventUseCase @Inject constructor(
    private val storeRepository: StoreRepository,
) {
    suspend operator fun invoke(
    ): List<StoreEvent> {
        return storeRepository.getStoreEvents()
    }
}