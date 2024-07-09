package `in`.koreatech.koin.domain.usecase.store

import `in`.koreatech.koin.domain.model.store.StoreCategories
import `in`.koreatech.koin.domain.model.store.StoreEvent
import `in`.koreatech.koin.domain.repository.StoreRepository
import javax.inject.Inject

class GetStoreCategoriesUseCase @Inject constructor(
    private val storeRepository: StoreRepository,
) {
    suspend operator fun invoke(
    ): List<StoreCategories> {
        return storeRepository.getStoreCategories()
    }
}