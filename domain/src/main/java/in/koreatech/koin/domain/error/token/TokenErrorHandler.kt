package `in`.koreatech.koin.domain.error.token

import `in`.koreatech.koin.domain.model.Result

interface TokenErrorHandler {
    fun handleLogoutError(throwable: Throwable): Result.Error
}