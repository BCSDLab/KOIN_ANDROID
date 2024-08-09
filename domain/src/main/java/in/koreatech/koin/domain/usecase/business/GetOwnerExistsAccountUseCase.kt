package `in`.koreatech.koin.domain.usecase.business

import `in`.koreatech.koin.domain.error.owner.OwnerError
import `in`.koreatech.koin.domain.error.user.UserErrorHandler
import `in`.koreatech.koin.domain.model.error.ErrorHandler
import `in`.koreatech.koin.domain.repository.OwnerSignupRepository
import javax.inject.Inject

class GetOwnerExistsAccountUseCase @Inject constructor(
    private val ownerSignupRepository: OwnerSignupRepository,
) {
    suspend operator fun invoke(
        phoneNumber: String,
    ): Pair<Boolean?, String?> {
        return try {
            ownerSignupRepository.getExistsAccount(phoneNumber).let{
                Pair(it.first, it.second)
            }
        } catch (t: Throwable) {
            null to t.message
        }
    }
}
