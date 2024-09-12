package `in`.koreatech.koin.domain.usecase.business

import `in`.koreatech.koin.domain.error.owner.OwnerErrorHandler
import `in`.koreatech.koin.domain.model.error.ErrorHandler
import `in`.koreatech.koin.domain.repository.OwnerSignupRepository
import javax.inject.Inject

class GetOwnerExistsAccountUseCase @Inject constructor(
    private val ownerSignupRepository: OwnerSignupRepository,
    private val ownerError: OwnerErrorHandler,
) {
    suspend operator fun invoke(
        phoneNumber: String,
    ): Pair<Boolean?, ErrorHandler?> {
        return try {
            ownerSignupRepository.getExistsAccount(phoneNumber).let {
                Pair(false, null)
            }
        } catch (t: Throwable) {
            ownerError.handleExistsAccountError(t)
        }
    }
}
