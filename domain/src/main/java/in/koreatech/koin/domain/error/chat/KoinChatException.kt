package `in`.koreatech.koin.domain.error.chat

import `in`.koreatech.koin.domain.error.KoinErrorException

sealed class KoinChatException : KoinErrorException() {
    /*
     * Exceptions for 403 Forbidden
     */
    class BlockedException : KoinChatException()
}
