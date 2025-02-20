package `in`.koreatech.koin.domain.usecase.business.menu

import `in`.koreatech.koin.domain.model.owner.menu.StoreMenuOptionPrice
import `in`.koreatech.koin.domain.repository.OwnerRegisterRepository
import javax.inject.Inject

class DeleteMenuUseCase @Inject constructor(
    private val ownerRegisterRepository: OwnerRegisterRepository
) {
    suspend operator fun invoke(
        menuId: Int
    ): Result<Unit> {
        return ownerRegisterRepository.storeMenuDelete(
            menuId
        )
    }
}
