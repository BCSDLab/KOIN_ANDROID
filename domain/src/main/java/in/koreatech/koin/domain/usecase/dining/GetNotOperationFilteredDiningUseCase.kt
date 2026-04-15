package `in`.koreatech.koin.domain.usecase.dining

import `in`.koreatech.koin.domain.model.dining.Dining
import `in`.koreatech.koin.domain.repository.DiningRepository
import javax.inject.Inject

class GetNotOperationFilteredDiningUseCase @Inject constructor(
    private val diningRepository: DiningRepository
) {
    suspend operator fun invoke(date: String): Result<List<Dining>> {
        return runCatching {
            diningRepository.getDining(date).filter { dining -> dining.menu.isNotEmpty() && dining.menu.first() != "미운영" }
        }
    }
}
