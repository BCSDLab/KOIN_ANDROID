package `in`.koreatech.koin.domain.usecase.signup

import `in`.koreatech.koin.domain.model.user.CodeCount
import `in`.koreatech.koin.domain.repository.UserRepository
import javax.inject.Inject

class RequestSmsVerificationUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(phoneNumber: String): Result<CodeCount> {
        return userRepository.requestSmsVerification(phoneNumber)
    }
}
