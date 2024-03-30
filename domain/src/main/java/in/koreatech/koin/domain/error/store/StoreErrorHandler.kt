package `in`.koreatech.koin.domain.error.store

import org.xml.sax.ErrorHandler

interface StoreErrorHandler {
    fun getStoreInfoError(throwable: Throwable): ErrorHandler
}