package `in`.koreatech.koin.domain.usecase.coopshop

import `in`.koreatech.koin.domain.model.coopshop.CoopShop
import `in`.koreatech.koin.domain.repository.CoopShopRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetCoopShopAllUseCase @Inject constructor(
    private val coopShopRepository: CoopShopRepository
) {
    operator fun invoke(): Flow<List<CoopShop>> = coopShopRepository.getCoopShopAll()
}
