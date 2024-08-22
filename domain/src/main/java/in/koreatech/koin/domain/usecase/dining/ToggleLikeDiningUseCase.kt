package `in`.koreatech.koin.domain.usecase.dining

import `in`.koreatech.koin.domain.model.dining.Dining
import `in`.koreatech.koin.domain.model.dining.LikeActionType
import `in`.koreatech.koin.domain.repository.DiningRepository
import javax.inject.Inject

class ToggleLikeDiningUseCase @Inject constructor(
    private val diningRepository: DiningRepository
) {
    suspend operator fun invoke(dining: Dining): Result<LikeActionType> {
        val isLiked: Boolean = dining.isLiked
        return if (isLiked) {
            unlikeDining(dining.id)
        } else {
            likeDining(dining.id)
        }
    }

    private suspend fun likeDining(id: Int): Result<LikeActionType> {
        return Result.runCatching {
            diningRepository.likeDining(id)
            LikeActionType.LIKE
        }
    }

    private suspend fun unlikeDining(id: Int): Result<LikeActionType> {
        return Result.runCatching {
            diningRepository.unlikeDining(id)
            LikeActionType.UNLIKE
        }
    }
}