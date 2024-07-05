package `in`.koreatech.koin.domain.usecase.business

import `in`.koreatech.koin.domain.constant.ERROR_OWNERSHOP_NOT_SET
import `in`.koreatech.koin.domain.error.owner.OwnerErrorHandler
import `in`.koreatech.koin.domain.model.error.ErrorHandler
import `in`.koreatech.koin.domain.model.store.StoreWithMenu
import `in`.koreatech.koin.domain.repository.OwnerShopRepository
import javax.inject.Inject

class GetOwnerShopInfoUseCase @Inject constructor(
    private val storeRepository: OwnerShopRepository,
    private val errorHandler: OwnerErrorHandler,
) {
    suspend operator fun invoke(
        storeId: Int
    ): Pair<StoreWithMenu?, ErrorHandler?> {
        return try {
            if (storeId < 0) {
                throw IllegalArgumentException(ERROR_OWNERSHOP_NOT_SET)
            }
            storeRepository.getOwnerShopInfo(storeId) to null
        } catch (throwable: Throwable) {
            null to errorHandler.handleGetOwnerInfoError(throwable)
        }
    }
}