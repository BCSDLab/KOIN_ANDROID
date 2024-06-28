package `in`.koreatech.koin.domain.usecase.business

import `in`.koreatech.koin.domain.model.store.Store
import `in`.koreatech.koin.domain.repository.StoreRepository
import javax.inject.Inject

class GetOwnerShopListUseCase @Inject constructor(
    private val storeRepository: StoreRepository
) {
    suspend operator fun invoke(): List<Store> {
        return storeRepository.getMyShopList()
    }
}