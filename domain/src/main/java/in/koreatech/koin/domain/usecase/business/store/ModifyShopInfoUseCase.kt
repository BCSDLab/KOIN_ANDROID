package `in`.koreatech.koin.domain.usecase.business.store

import `in`.koreatech.koin.domain.error.owner.OwnerErrorHandler
import `in`.koreatech.koin.domain.model.error.ErrorHandler
import `in`.koreatech.koin.domain.model.owner.StoreDetailInfo
import `in`.koreatech.koin.domain.model.owner.insertstore.OperatingTime
import `in`.koreatech.koin.domain.model.store.StoreWithMenu
import `in`.koreatech.koin.domain.repository.OwnerShopRepository
import javax.inject.Inject

class ModifyShopInfoUseCase @Inject constructor(
    private val storeRepository: OwnerShopRepository,
    private val errorHandler: OwnerErrorHandler,
) {
    suspend operator fun invoke(
        shopId: Int,
        storeDetailInfo: StoreDetailInfo,
    ): ErrorHandler? {
        return try {
            storeRepository.modifyOwnerShopInfo(
                shopId,
                storeDetailInfo,
            )
            null
        } catch (throwable: Throwable) {
            errorHandler.handleModifyOwnerShopInfoError(throwable)
        }
    }
}