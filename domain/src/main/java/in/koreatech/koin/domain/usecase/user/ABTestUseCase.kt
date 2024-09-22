package `in`.koreatech.koin.domain.usecase.user

import `in`.koreatech.koin.domain.error.user.UserErrorHandler
import `in`.koreatech.koin.domain.model.error.ErrorHandler
import `in`.koreatech.koin.domain.repository.UserRepository
import `in`.koreatech.koin.domain.util.ext.toSHA256
import javax.inject.Inject

class ABTestUseCase  @Inject constructor(
    private val userRepository: UserRepository,
    private val userErrorHandler: UserErrorHandler
) {
    suspend operator fun invoke(title: String): ErrorHandler? {
        return try {
            userRepository.postABTestAssign(title)
            null
        } catch (t: Throwable) {
            userErrorHandler.handleVerifyUserPasswordError(t)
        }
    }
}