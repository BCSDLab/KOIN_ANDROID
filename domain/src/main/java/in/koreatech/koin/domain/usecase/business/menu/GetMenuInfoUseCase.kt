package `in`.koreatech.koin.domain.usecase.business.menu

import `in`.koreatech.koin.domain.model.owner.menu.StoreMenuInfo
import `in`.koreatech.koin.domain.repository.OwnerShopRepository

import javax.inject.Inject

class GetMenuInfoUseCase @Inject constructor(
    private val ownerShopRepository: OwnerShopRepository,
) {
    suspend operator fun invoke(
        menuId: Int
    ): StoreMenuInfo {
        return ownerShopRepository.getStoreMenuInfo(menuId)
    }
}