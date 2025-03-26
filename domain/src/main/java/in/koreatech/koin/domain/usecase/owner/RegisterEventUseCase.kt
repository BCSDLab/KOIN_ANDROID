package `in`.koreatech.koin.domain.usecase.owner

import `in`.koreatech.koin.domain.model.owner.EventInfo
import `in`.koreatech.koin.domain.repository.OwnerShopRepository
import javax.inject.Inject

class RegisterEventUseCase @Inject constructor(
    private val ownerShopRepository: OwnerShopRepository
) {
    suspend operator fun invoke(
        shopId: Int,
        eventInfo: EventInfo
    ) {
        ownerShopRepository.registerEvent(shopId, eventInfo)
    }
}
