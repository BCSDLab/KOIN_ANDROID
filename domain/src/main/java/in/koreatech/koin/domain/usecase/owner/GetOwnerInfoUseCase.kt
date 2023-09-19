package `in`.koreatech.koin.domain.usecase.owner

import `in`.koreatech.koin.domain.error.owner.OwnerErrorHandler
import `in`.koreatech.koin.domain.error.store.StoreErrorHandler
import `in`.koreatech.koin.domain.model.owner.Owner
import `in`.koreatech.koin.domain.repository.OwnerRepository
import `in`.koreatech.koin.domain.repository.StoreRepository
import `in`.koreatech.koin.domain.repository.TokenRepository
import org.xml.sax.ErrorHandler
import javax.inject.Inject

class GetOwnerInfoUseCase @Inject constructor(
    private val ownerRepository: OwnerRepository,
    private val tokenRepository: TokenRepository,
    private val ownerErrorHandler: OwnerErrorHandler,
    private val storeRepository: StoreRepository,
    private val storeErrorHandler: StoreErrorHandler
) {
    suspend operator fun invoke(): Pair<Owner?, ErrorHandler?> {
        return if(tokenRepository.getAccessToken() == null) {
            Owner.Anonymous to null
        } else {
            try {
                val owner = ownerRepository.getOwnerInfo()

                if(owner is Owner.Businessman && owner.shop != null) {
                    return try {
                        val shopInfo = storeRepository.getStore(owner.shop.id)
                        owner.copy(shop = shopInfo) to null
                    } catch (t: Throwable) {
                        owner to storeErrorHandler.getStoreInfoError(t)
                    }
                }
            } catch (t: Throwable) {
                null to ownerErrorHandler.handleGetOwnerInfoError(t)
            }

            return ownerRepository.getOwnerInfo() to null
        }
    }
}