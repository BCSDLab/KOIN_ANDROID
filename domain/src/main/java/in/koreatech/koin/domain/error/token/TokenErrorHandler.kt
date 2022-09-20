package `in`.koreatech.koin.domain.error.token

import `in`.koreatech.koin.domain.model.error.ErrorHandler

interface TokenErrorHandler {
    fun handleLogoutError(throwable: Throwable): ErrorHandler
}