package `in`.koreatech.koin.domain.usecase.coopshop

import `in`.koreatech.koin.domain.model.coopshop.CoopShop
import `in`.koreatech.koin.domain.repository.CoopShopRepository
import javax.inject.Inject

class GetCoopShopAllUseCase @Inject constructor(
    private val coopShopRepository: CoopShopRepository
) {
    suspend operator fun invoke(): Result<List<CoopShop>> {
        return coopShopRepository.getCoopShopAll()
    }
}
