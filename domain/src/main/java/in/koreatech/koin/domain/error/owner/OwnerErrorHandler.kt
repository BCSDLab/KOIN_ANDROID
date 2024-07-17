package `in`.koreatech.koin.domain.error.owner

import `in`.koreatech.koin.domain.model.error.ErrorHandler

interface OwnerErrorHandler {
    fun handleGetTokenError(throwable: Throwable): ErrorHandler
    fun handleGetOwnerInfoError(throwable: Throwable): ErrorHandler
    fun handleGetOwnerShopListError(throwable: Throwable): ErrorHandler
    fun handleDeleteOwnerShopEventError(throwable: Throwable): ErrorHandler
    fun handleModifyOwnerShopInfoError(throwable: Throwable): ErrorHandler
}