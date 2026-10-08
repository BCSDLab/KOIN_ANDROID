package `in`.koreatech.koin.domain.usecase.dining

import `in`.koreatech.koin.domain.repository.DiningRepository
import `in`.koreatech.koin.domain.util.DiningUtil
import `in`.koreatech.koin.domain.util.TimeUtil
import javax.inject.Inject

class SyncDiningUseCase @Inject constructor(
    private val diningRepository: DiningRepository
) {
    suspend operator fun invoke(): Result<Unit> =
        diningRepository.fetchDining(TimeUtil.dateFormatToYYMMDD(DiningUtil.getCurrentDate()))
}
