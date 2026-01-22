package `in`.koreatech.koin.domain.error.article

import `in`.koreatech.koin.domain.error.KoinErrorException

/**
 * Exceptions related to article APIs.
 * Don't add article prefix because we using sealed class to group exceptions.
 * Every exceptions should ends with Exception.
 */

sealed class KoinArticleException : KoinErrorException() {
    /*
     * Exceptions for 400 Bad Request
     */
    class CanNotFoundItemException : KoinArticleException()

    /*
     * Exceptions for 401 Bad Request
     */
    class UnauthorizedUserException : KoinArticleException()

    /*
     * Exceptions for 403 Bad Request
     */
    class ForbiddenAuthor : KoinArticleException()

    /*
     * Exceptions for 404 Bad Request
     */
    class NotFoundImage : KoinArticleException()
}
