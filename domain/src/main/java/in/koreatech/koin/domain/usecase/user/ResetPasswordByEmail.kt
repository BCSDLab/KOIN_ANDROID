package `in`.koreatech.koin.domain.usecase.user

import `in`.koreatech.koin.domain.repository.UserRepository
import `in`.koreatech.koin.domain.util.ext.toSHA256
import javax.inject.Inject

class ResetPasswordByEmail @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(loginId: String, email: String, newPassword: String): Result<Unit> {
        return runCatching {
            userRepository.resetPasswordByEmail(
                loginId = loginId,
                email = email,
                newPassword = newPassword.toSHA256()
            )
        }
    }
}
