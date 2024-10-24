package `in`.koreatech.koin.domain.usecase.business

import `in`.koreatech.koin.domain.model.store.StoreMenu
import `in`.koreatech.koin.domain.repository.OwnerShopRepository
import `in`.koreatech.koin.domain.repository.StoreRepository
import javax.inject.Inject

class GetOwnerShopMenusUseCase @Inject constructor(
    private val storeRepository: OwnerShopRepository
) {
    suspend operator fun invoke(storeId: Int): StoreMenu {
        return storeRepository.getOwnerShopMenus(storeId)
    }
}