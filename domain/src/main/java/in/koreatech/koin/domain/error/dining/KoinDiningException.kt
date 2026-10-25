package `in`.koreatech.koin.domain.error.dining

import `in`.koreatech.koin.domain.error.KoinErrorException

sealed class KoinDiningException : KoinErrorException() {
    /*
     * Exceptions for 400
     */
    class DiningReportDateNotAllowedException : KoinDiningException()
    class InvalidReportImageException : KoinDiningException()

    /*
     * Exceptions for 404
     */
    class NotFoundDiningException : KoinDiningException()

    /*
     * Exceptions for 409
     */
    class DiningAlreadySoldOutException : KoinDiningException()
    class DiningReportAlreadySubmittedException : KoinDiningException()
}
