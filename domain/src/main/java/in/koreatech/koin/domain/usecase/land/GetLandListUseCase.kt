package `in`.koreatech.koin.domain.usecase.land

import `in`.koreatech.koin.domain.model.land.Land
import `in`.koreatech.koin.domain.repository.LandRepository
import javax.inject.Inject

class GetLandListUseCase @Inject constructor(
    private val landRepository: LandRepository
) {
    suspend operator fun invoke(): Result<List<Land>> {
        return kotlin.runCatching {
            landRepository.getLandList()
        }
    }
}