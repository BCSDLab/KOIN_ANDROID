package `in`.koreatech.koin.domain.error.coopshop

import `in`.koreatech.koin.domain.error.KoinErrorException

/**
 * Exceptions related to coopshop APIs.
 * Don't add CoopShop prefix because we using sealed class to group exceptions.
 * Every exceptions should ends with Exception.
 */
sealed class KoinCoopShopException : KoinErrorException() {
    /*
     * Exceptions for 404 Not Found
     */
    class CoopShopNotFoundException : KoinCoopShopException()
}
