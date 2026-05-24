package `in`.koreatech.koin.domain.usecase.user

import `in`.koreatech.koin.domain.constant.ERROR_FORGOTPASSWORD_BLANK_ACCOUNT
import `in`.koreatech.koin.domain.repository.UserRepository
import javax.inject.Inject
import kotlin.Result

class RequestFindPasswordEmailUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(email: String): Result<Unit> {
        if (email.isBlank()) {
            return Result.failure(IllegalArgumentException(ERROR_FORGOTPASSWORD_BLANK_ACCOUNT))
        }
        return userRepository.requestPasswordResetEmail(email)
    }
}
