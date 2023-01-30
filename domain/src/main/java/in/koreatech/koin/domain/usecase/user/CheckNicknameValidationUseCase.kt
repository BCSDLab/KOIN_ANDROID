package `in`.koreatech.koin.domain.usecase.user

import `in`.koreatech.koin.domain.error.user.UserErrorHandler
import `in`.koreatech.koin.domain.model.error.ErrorHandler
import `in`.koreatech.koin.domain.repository.UserRepository
import javax.inject.Inject
import `in`.koreatech.koin.domain.model.Result
import `in`.koreatech.koin.domain.model.toResult

class CheckNicknameValidationUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val userErrorHandler: UserErrorHandler
) {
    suspend operator fun invoke(nickname: String) : Result<Boolean> {
        return try {
            if(nickname.isBlank()) throw IllegalArgumentException()
            userRepository.isUsernameDuplicated(nickname).toResult()
        } catch (t: Throwable) {
            userErrorHandler.handleUsernameDuplicatedError(t)
        }
    }
}