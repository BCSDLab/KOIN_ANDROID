package `in`.koreatech.koin.domain.usecase.coopshop

import `in`.koreatech.koin.domain.error.coopshop.CoopShopErrorHandler
import `in`.koreatech.koin.domain.model.coopshop.CoopShop
import `in`.koreatech.koin.domain.model.coopshop.CoopShopType
import `in`.koreatech.koin.domain.model.error.ErrorHandler
import `in`.koreatech.koin.domain.repository.CoopShopRepository
import javax.inject.Inject

class GetCoopShopUseCase @Inject constructor(
    private val coopShopRepository: CoopShopRepository,
    private val coopShopErrorHandler: CoopShopErrorHandler
) {
    suspend operator fun invoke(type: CoopShopType): Pair<CoopShop?, ErrorHandler?> {
        return try {
            coopShopRepository.getCoopShopById(type.id) to null
        } catch (t: Throwable) {
            null to coopShopErrorHandler.handleGetCoopShopError(t)
        }
    }
}