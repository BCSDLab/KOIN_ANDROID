package `in`.koreatech.koin.domain.usecase.owner

import `in`.koreatech.koin.domain.repository.OwnerShopRepository
import `in`.koreatech.koin.domain.repository.UserRepository
import javax.inject.Inject

class OwnerHasStoreUseCase @Inject constructor(
    private val ownerShopRepository: OwnerShopRepository
) {
    operator fun invoke(): Boolean {
        return ownerShopRepository.getOwnerStoreSize()
    }
}