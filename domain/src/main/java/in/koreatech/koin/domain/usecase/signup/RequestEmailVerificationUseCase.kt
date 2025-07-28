package `in`.koreatech.koin.domain.usecase.signup

import `in`.koreatech.koin.domain.model.user.CodeCount
import `in`.koreatech.koin.domain.repository.UserRepository
import javax.inject.Inject

class RequestEmailVerificationUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(email: String): Result<CodeCount> {
        return userRepository.requestEmailVerification(email)
    }
}
