package `in`.koreatech.koin.domain.usecase.user

import `in`.koreatech.koin.domain.error.user.UserErrorHandler
import `in`.koreatech.koin.domain.model.error.ErrorHandler
import `in`.koreatech.koin.domain.repository.UserRepository
import javax.inject.Inject

class CheckEmailValidationUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val userErrorHandler: UserErrorHandler
) {
    suspend operator fun invoke(email: String) : Pair<Boolean?, ErrorHandler?> {
        return try {
            if(email.isBlank()) throw IllegalArgumentException()
            userRepository.isUserEmailDuplicated(email) to null
        } catch (t: Throwable) {
            null to userErrorHandler.handleUsernameDuplicatedError(t)
        }
    }
}