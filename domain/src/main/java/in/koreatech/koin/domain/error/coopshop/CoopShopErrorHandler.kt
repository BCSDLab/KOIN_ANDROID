package `in`.koreatech.koin.domain.error.coopshop

import `in`.koreatech.koin.domain.model.error.ErrorHandler

interface CoopShopErrorHandler {
    fun handleGetCoopShopAllError(throwable: Throwable) : ErrorHandler
    fun handleGetCoopShopError(throwable: Throwable) : ErrorHandler
}