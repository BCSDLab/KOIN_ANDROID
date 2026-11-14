package `in`.koreatech.koin.domain.error.chat

import `in`.koreatech.koin.domain.error.KoinErrorException

sealed class KoinChatException : KoinErrorException() {
    class BlockedException : KoinChatException()

    class UnauthorizedException : KoinChatException()

    class AccessDeniedException : KoinChatException()

    class RoomNotFoundException : KoinChatException()
}
