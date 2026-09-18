package `in`.koreatech.koin.domain.usecase.coopshop

import `in`.koreatech.koin.domain.model.coopshop.CoopShop
import `in`.koreatech.koin.domain.model.coopshop.CoopShopType
import `in`.koreatech.koin.domain.repository.CoopShopRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetCoopShopUseCase @Inject constructor(
    private val coopShopRepository: CoopShopRepository
) {
    operator fun invoke(type: CoopShopType): Flow<CoopShop?> = coopShopRepository.getCoopShopById(type.id)
}
