package `in`.koreatech.koin.domain.error.notification

import `in`.koreatech.koin.domain.error.KoinErrorException

sealed class KoinNotificationException : KoinErrorException() {
    class BadRequestException : KoinNotificationException()
    class UnauthorizedException : KoinNotificationException()
    class ForbiddenException : KoinNotificationException()
    class NotFoundException : KoinNotificationException()
}
