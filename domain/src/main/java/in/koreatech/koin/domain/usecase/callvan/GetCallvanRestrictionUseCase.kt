package `in`.koreatech.koin.domain.usecase.callvan

import `in`.koreatech.koin.domain.model.callvan.CallvanRestriction
import `in`.koreatech.koin.domain.repository.CallvanRepository
import javax.inject.Inject

class GetCallvanRestrictionUseCase @Inject constructor(
    private val callvanRepository: CallvanRepository
) {
    suspend operator fun invoke(): Result<CallvanRestriction> = callvanRepository.getCallvanRestriction()
}
