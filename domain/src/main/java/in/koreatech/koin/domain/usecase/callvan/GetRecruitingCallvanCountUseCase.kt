package `in`.koreatech.koin.domain.usecase.callvan

import `in`.koreatech.koin.domain.repository.CallvanRepository
import javax.inject.Inject

class GetRecruitingCallvanCountUseCase @Inject constructor(
    private val callvanRepository: CallvanRepository
) {
    suspend operator fun invoke(): Result<Int> = callvanRepository.getRecruitingCallvanCount()
}
