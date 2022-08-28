package `in`.koreatech.koin.domain.error.user

import `in`.koreatech.koin.domain.model.error.ErrorHandler

interface UserErrorHandler {
    fun handleRequestPasswordResetEmailError(throwable: Throwable): ErrorHandler
}