package `in`.koreatech.koin.domain.usecase.business.menu

import `in`.koreatech.koin.domain.model.owner.menu.StoreMenuOptionPrice
import `in`.koreatech.koin.domain.repository.OwnerRegisterRepository
import javax.inject.Inject

class ModifyMenuUseCase @Inject constructor(
    private val ownerRegisterRepository: OwnerRegisterRepository
) {
    suspend operator fun invoke(
        menuId: Int,
        menuCategoryId: List<Int>,
        description: String,
        menuImageUrlList: List<String>,
        menuName: String,
        menuOptionPrice: List<StoreMenuOptionPrice>,
        menuSinglePrice: String
    ): Result<Unit> {
        return ownerRegisterRepository.storeMenuModify(
            menuId = menuId,
            menuCategoryId = menuCategoryId,
            description = description,
            menuImageUrlList = menuImageUrlList,
            isSingle = menuOptionPrice.isEmpty(),
            menuName = menuName,
            menuOptionPrice = menuOptionPrice,
            menuSinglePrice = menuSinglePrice
        )
    }
}