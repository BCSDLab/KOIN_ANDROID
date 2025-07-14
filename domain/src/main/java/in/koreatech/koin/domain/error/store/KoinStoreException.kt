package `in`.koreatech.koin.domain.error.store

import `in`.koreatech.koin.domain.error.KoinErrorException

sealed class KoinStoreException : KoinErrorException() {
    /*
     * Exceptions for 400 Bad Request
     */
    class RequiredOptionGroupMissingException : KoinStoreException()
    class MinimumSelectionNotMetException : KoinStoreException()
    class MaxSelectionExceededException : KoinStoreException()
    class InvalidOptionInGroupException : KoinStoreException()
    class BadRequestException : KoinStoreException()
    class InvalidQuantityException : KoinStoreException()
    class DifferentShopItemInCartException : KoinStoreException()
    class MenuSoldOutException : KoinStoreException()
    class InvalidMenuInShopException : KoinStoreException()
    class ShopClosedException : KoinStoreException()
    class ShopNotDeliverableException : KoinStoreException()
    class ShopNotTakeoutAvailableException : KoinStoreException()
    class OrderAmountBelowMinimumException : KoinStoreException()

    /*
     * Exceptions for 401 Unauthorized
     */
    class UnauthorizedException : KoinStoreException()

    /*
     * Exceptions for 404 Not Found
     */
    class ShopNotFoundException : KoinStoreException()
    class MenuNotFoundException : KoinStoreException()
    class CartItemNotFoundException : KoinStoreException()
    class MenuPriceNotFoundException : KoinStoreException()
    class MenuOptionNotFoundException : KoinStoreException()
    class CartNotFoundException : KoinStoreException()
}
