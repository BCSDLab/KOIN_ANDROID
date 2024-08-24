package `in`.koreatech.koin.domain.usecase.business.menu

import `in`.koreatech.koin.domain.model.owner.menu.StoreMenuCategory
import `in`.koreatech.koin.domain.model.owner.menu.StoreMenuOptionPrice
import `in`.koreatech.koin.domain.repository.OwnerRegisterRepository
import `in`.koreatech.koin.domain.repository.StoreRepository
import javax.inject.Inject

class RegisterMenuUseCase @Inject constructor(
    private val ownerRegisterRepository: OwnerRegisterRepository
) {
    suspend operator fun invoke(
        storeId: Int,
        menuCategoryId: List<Int>,
        description: String,
        menuImageUrlList: List<String>,
        menuName: String,
        menuOptionPrice: List<StoreMenuOptionPrice>,
        menuSinglePrice: String
    ): Result<Unit> {
        return ownerRegisterRepository.storeMenuRegister(
            storeId = storeId,
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
