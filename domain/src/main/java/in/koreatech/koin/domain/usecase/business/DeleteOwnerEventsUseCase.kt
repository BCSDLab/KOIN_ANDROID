package `in`.koreatech.koin.domain.usecase.business

import `in`.koreatech.koin.domain.error.owner.OwnerErrorHandler
import `in`.koreatech.koin.domain.repository.OwnerShopRepository
import javax.inject.Inject

class DeleteOwnerEventsUseCase @Inject constructor(
    private val storeRepository: OwnerShopRepository,
    private val ownerErrorHandler: OwnerErrorHandler
) {
    suspend operator fun invoke(shopId: Int, eventId: Int) {
        try {
            storeRepository.deleteOwnerShopEvent(shopId, eventId)
        } catch (throwable: Throwable) {
            ownerErrorHandler.handleDeleteOwnerShopEventError(throwable)
        }
    }
}
