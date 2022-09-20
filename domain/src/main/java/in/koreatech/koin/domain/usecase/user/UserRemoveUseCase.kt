package `in`.koreatech.koin.domain.usecase.user

import `in`.koreatech.koin.domain.error.user.UserErrorHandler
import `in`.koreatech.koin.domain.model.error.ErrorHandler
import `in`.koreatech.koin.domain.repository.UserRepository
import javax.inject.Inject

class UserRemoveUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val userErrorHandler: UserErrorHandler
) {
    suspend operator fun invoke() : Pair<Unit, ErrorHandler?> {
        return Unit to try {
            userRepository.deleteUser()
            null
        } catch (t: Throwable) {
            userErrorHandler.handleDeleteUserError(t)
        }
    }
}