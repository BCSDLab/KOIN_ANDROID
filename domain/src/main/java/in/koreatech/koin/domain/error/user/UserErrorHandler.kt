package `in`.koreatech.koin.domain.error.user

import `in`.koreatech.koin.domain.model.error.ErrorHandler

interface UserErrorHandler {
    fun handleRequestPasswordResetEmailError(throwable: Throwable): ErrorHandler
    fun handleGetUserInfoError(throwable: Throwable): ErrorHandler
    fun handleDeleteUserError(throwable: Throwable): ErrorHandler
    fun handleUsernameDuplicatedError(throwable: Throwable): ErrorHandler
    fun handleUpdateUserError(throwable: Throwable): ErrorHandler
}