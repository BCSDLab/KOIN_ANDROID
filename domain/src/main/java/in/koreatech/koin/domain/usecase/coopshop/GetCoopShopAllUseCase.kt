package `in`.koreatech.koin.domain.usecase.coopshop

import `in`.koreatech.koin.domain.error.coopshop.CoopShopErrorHandler
import `in`.koreatech.koin.domain.model.coopshop.CoopShop
import `in`.koreatech.koin.domain.model.error.ErrorHandler
import `in`.koreatech.koin.domain.repository.CoopShopRepository
import javax.inject.Inject

class GetCoopShopAllUseCase @Inject constructor(
    private val coopShopRepository: CoopShopRepository,
    private val coopShopErrorHandler: CoopShopErrorHandler
)  {
    suspend operator fun invoke(): Pair<List<CoopShop>?, ErrorHandler?> {
        return try {
            coopShopRepository.getCoopShopAll() to null
        } catch (t: Throwable) {
            null to coopShopErrorHandler.handleGetCoopShopAllError(t)
        }
    }
}