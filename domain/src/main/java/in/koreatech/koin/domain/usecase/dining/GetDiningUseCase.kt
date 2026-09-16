package `in`.koreatech.koin.domain.usecase.dining

import `in`.koreatech.koin.domain.model.dining.Dining
import `in`.koreatech.koin.domain.repository.DiningRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetDiningUseCase @Inject constructor(
    private val diningRepository: DiningRepository
) {
    operator fun invoke(date: String): Flow<List<Dining>> = diningRepository.getDining(date)
}
