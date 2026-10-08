package `in`.koreatech.koin.domain.usecase.coopshop

import `in`.koreatech.koin.domain.repository.CoopShopRepository
import javax.inject.Inject

class SyncCoopShopUseCase @Inject constructor(
    private val coopShopRepository: CoopShopRepository
) {
    suspend operator fun invoke(): Result<Unit> = coopShopRepository.sync()
}
