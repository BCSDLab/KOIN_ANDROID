package `in`.koreatech.koin.domain.usecase.user

import `in`.koreatech.koin.domain.error.user.UserErrorHandler
import `in`.koreatech.koin.domain.model.Result
import `in`.koreatech.koin.domain.repository.UserRepository
import javax.inject.Inject

class UserRemoveUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val userErrorHandler: UserErrorHandler
) {
    suspend operator fun invoke(): Result<Unit> {
        return try {
            userRepository.deleteUser()
            Result.Success(Unit)
        } catch (t: Throwable) {
            userErrorHandler.handleDeleteUserError(t)
        }
    }
}