package `in`.koreatech.koin.domain.error.recruitment

import `in`.koreatech.koin.domain.error.KoinErrorException

sealed class KoinRecruitmentChatException : KoinErrorException() {
    /*
     * Exceptions for 400
     */
    class InvalidParameterException : KoinRecruitmentChatException()
    class NotReadableHttpMessageException : KoinRecruitmentChatException()
    class InvalidRequestBodyException : KoinRecruitmentChatException()

    /*
     * Exceptions for 401
     */
    class UnauthorizedException : KoinRecruitmentChatException()

    /*
     * Exceptions for 403
     */
    class ChatMemberForbiddenException : KoinRecruitmentChatException()
    class DirectChatForbiddenException : KoinRecruitmentChatException()

    /*
     * Exceptions for 404
     */
    class ChatRoomNotFoundException : KoinRecruitmentChatException()
    class ApplicationNotFoundException : KoinRecruitmentChatException()

    /*
     * Exceptions for 409
     */
    class DirectChatConflictException : KoinRecruitmentChatException()
    class ChatReadOnlyException : KoinRecruitmentChatException()
    class RequestTooFastException : KoinRecruitmentChatException()
}
