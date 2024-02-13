package `in`.koreatech.koin.domain.usecase.store

import `in`.koreatech.koin.domain.model.store.StoreMenu
import `in`.koreatech.koin.domain.repository.StoreRepository
import javax.inject.Inject

class GetShopMenusUseCase @Inject constructor(
    private val storeRepository: StoreRepository
) {
    suspend operator fun invoke(storeId: Int): StoreMenu {
        return storeRepository.getShopMenus(storeId)
    }
}