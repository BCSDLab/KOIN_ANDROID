package `in`.koreatech.koin.domain.error.owner

import `in`.koreatech.koin.domain.model.error.ErrorHandler

interface OwnerErrorHandler {
    fun handleGetTokenError(throwable: Throwable): ErrorHandler
    fun handleRequestPasswordResetEmailError(throwable: Throwable): ErrorHandler
    fun handleGetOwnerInfoError(throwable: Throwable): ErrorHandler
    fun handleDeleteOwnerError(throwable: Throwable): ErrorHandler
    fun handleUpdateOwnerError(throwable: Throwable): ErrorHandler
}