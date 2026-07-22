package `in`.koreatech.koin.domain.usecase.coopshop

import `in`.koreatech.koin.domain.model.coopshop.CoopShop
import `in`.koreatech.koin.domain.model.coopshop.CoopShopType
import `in`.koreatech.koin.domain.repository.CoopShopRepository
import javax.inject.Inject

class GetCoopShopUseCase @Inject constructor(
    private val coopShopRepository: CoopShopRepository
) {
    suspend operator fun invoke(type: CoopShopType): Result<CoopShop> {
        return coopShopRepository.getCoopShopById(type.id)
    }
}
