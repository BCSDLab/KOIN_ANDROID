package `in`.koreatech.koin.domain.usecase.signup

import `in`.koreatech.koin.domain.repository.UserRepository
import javax.inject.Inject

class CheckEmailValidationUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(email: String): Result<Boolean> {
        return userRepository.isUserEmailDuplicated(email)
    }
}
// TODO: Remove after new sign up release
