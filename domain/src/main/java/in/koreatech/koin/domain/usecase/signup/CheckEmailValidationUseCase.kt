package `in`.koreatech.koin.domain.usecase.signup

import `in`.koreatech.koin.domain.error.user.UserErrorHandler
import `in`.koreatech.koin.domain.model.error.ErrorHandler
import `in`.koreatech.koin.domain.repository.UserRepository
import `in`.koreatech.koin.domain.state.signup.SignupContinuationState
import javax.inject.Inject

class CheckEmailValidationUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val userErrorHandler: UserErrorHandler
) {
    suspend operator fun invoke(email: String) : Pair<Boolean?, SignupContinuationState?> {
      return when{
          email.isBlank() -> null to SignupContinuationState.EmailIsNotValidate
          else -> userRepository.isUserEmailDuplicated(email) to null
        }
    }
}