package `in`.koreatech.koin.domain.usecase.dining

import `in`.koreatech.koin.domain.model.dining.Dining
import `in`.koreatech.koin.domain.repository.DiningRepository
import `in`.koreatech.koin.domain.util.TimeUtil
import java.util.Date
import javax.inject.Inject

class DiningUseCase @Inject constructor(
    private val diningRepository: DiningRepository
) {
    suspend operator fun invoke(date: Date): Result<List<Dining>> {
        return kotlin.runCatching {
            diningRepository.getDining(TimeUtil.dateFormatToYYMMDD(date))
        }
    }
}