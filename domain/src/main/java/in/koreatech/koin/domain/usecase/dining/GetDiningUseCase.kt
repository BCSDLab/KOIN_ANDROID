package `in`.koreatech.koin.domain.usecase.dining

import `in`.koreatech.koin.domain.model.dining.Dining
import `in`.koreatech.koin.domain.repository.DiningRepository
import `in`.koreatech.koin.domain.repository.TokenRepository
import javax.inject.Inject

class GetDiningUseCase @Inject constructor(
    private val diningRepository: DiningRepository,
    private val tokenRepository: TokenRepository
) {
    suspend operator fun invoke(date: String): Result<List<Dining>> {
        if(tokenRepository.getAccessToken() == null) {  // 익명
            return kotlin.runCatching {
                diningRepository.getDining(date)
            }
        } else {    // 로그인 유저
            return kotlin.runCatching {
                diningRepository.getAuthDining(date)
            }
        }
    }
}