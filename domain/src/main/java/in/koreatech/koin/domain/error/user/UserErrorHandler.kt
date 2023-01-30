package `in`.koreatech.koin.domain.error.user

import `in`.koreatech.koin.domain.model.Result

interface UserErrorHandler {
    fun handleGetTokenError(throwable: Throwable): Result.Error
    fun handleRequestPasswordResetEmailError(throwable: Throwable): Result.Error
    fun handleGetUserInfoError(throwable: Throwable): Result.Error
    fun handleDeleteUserError(throwable: Throwable): Result.Error
    fun handleUsernameDuplicatedError(throwable: Throwable): Result.Error
    fun handleUpdateUserError(throwable: Throwable): Result.Error
}
