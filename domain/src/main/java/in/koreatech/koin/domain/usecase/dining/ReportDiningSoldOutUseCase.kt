package `in`.koreatech.koin.domain.usecase.dining

import `in`.koreatech.koin.domain.repository.DiningRepository
import javax.inject.Inject

class ReportDiningSoldOutUseCase @Inject constructor(
    private val diningRepository: DiningRepository
) {
    suspend operator fun invoke(diningId: Int, imageUrl: String): Result<Unit> {
        return diningRepository.postDiningSoldOutReport(diningId, imageUrl)
    }
}
