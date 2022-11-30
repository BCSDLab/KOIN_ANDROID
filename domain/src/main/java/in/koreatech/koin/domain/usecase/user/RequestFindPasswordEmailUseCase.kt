package `in`.koreatech.koin.domain.usecase.user

import `in`.koreatech.koin.domain.constant.ERROR_FORGOTPASSWORD_BLANK_ACCOUNT
import `in`.koreatech.koin.domain.error.user.UserErrorHandler
import `in`.koreatech.koin.domain.model.error.ErrorHandler
import `in`.koreatech.koin.domain.repository.UserRepository
import javax.inject.Inject

class RequestFindPasswordEmailUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val userErrorHandler: UserErrorHandler
) {
    suspend operator fun invoke(portalAccount: String): ErrorHandler? {
        if (portalAccount.isBlank()){
            return userErrorHandler.handleRequestPasswordResetEmailError(
                IllegalArgumentException(ERROR_FORGOTPASSWORD_BLANK_ACCOUNT)
            )
        }
        return try {
            userRepository.requestPasswordResetEmail(portalAccount)
            null
        } catch (t: Throwable) {
            userErrorHandler.handleRequestPasswordResetEmailError(t)
        }
    }
}