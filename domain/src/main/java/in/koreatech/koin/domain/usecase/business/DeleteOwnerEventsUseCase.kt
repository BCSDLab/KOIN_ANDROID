package `in`.koreatech.koin.domain.usecase.business

import `in`.koreatech.koin.domain.constant.ERROR_OWNERSHOP_NOT_SET
import `in`.koreatech.koin.domain.error.owner.OwnerErrorHandler
import `in`.koreatech.koin.domain.model.error.ErrorHandler
import `in`.koreatech.koin.domain.model.store.StoreWithMenu
import `in`.koreatech.koin.domain.repository.OwnerShopRepository
import javax.inject.Inject

class DeleteOwnerEventsUseCase @Inject constructor(
    private val storeRepository: OwnerShopRepository,
) {
    suspend operator fun invoke(shopId:Int, eventId: Int) {
        try {
            storeRepository.deleteOwnerShopEvent(shopId, eventId)
        }
        catch (throwable: Throwable) {
            throw throwable
        }
    }
}
