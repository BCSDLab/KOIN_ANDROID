package `in`.koreatech.koin.domain.usecase.user

import `in`.koreatech.koin.domain.repository.UserRepository
import `in`.koreatech.koin.domain.util.ext.toSHA256
import javax.inject.Inject

class ResetPasswordBySms @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(loginId: String, phone: String, newPassword: String): Result<Unit> {
        return userRepository.resetPasswordBySms(
            loginId = loginId,
            phone = phone,
            newPassword = newPassword.toSHA256()
        )
    }
}
