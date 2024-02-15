package `in`.koreatech.koin.domain.error.owner

import `in`.koreatech.koin.domain.model.error.ErrorHandler

interface OwnerErrorHandler {
    fun handleGetOwnerInfoError(throwable: Throwable): ErrorHandler
}