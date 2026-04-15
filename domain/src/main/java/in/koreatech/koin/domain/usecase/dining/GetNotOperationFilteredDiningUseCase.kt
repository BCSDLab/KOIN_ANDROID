package `in`.koreatech.koin.domain.usecase.dining

import `in`.koreatech.koin.domain.model.dining.Dining
import `in`.koreatech.koin.domain.model.dining.DiningPlace
import `in`.koreatech.koin.domain.repository.DiningRepository
import javax.inject.Inject

class GetNotOperationFilteredDiningUseCase @Inject constructor(
    private val diningRepository: DiningRepository
) {
    suspend operator fun invoke(date: String): Result<List<Dining>> {
        return runCatching {
            diningRepository.getDining(date)
                .filter { dining -> dining.menu.isNotEmpty() && dining.menu.first() != "미운영" }
                .sortedBy { diningOrder[it.place] ?: Int.MAX_VALUE }
        }
    }
}

private val diningOrder = mapOf(
    DiningPlace.CornerA.place to 0,
    DiningPlace.CornerB.place to 1,
    DiningPlace.CornerC.place to 2,
    DiningPlace.Nungsu.place to 3,
    DiningPlace.Campus2.place to 4
)
