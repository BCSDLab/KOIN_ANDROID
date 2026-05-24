package `in`.koreatech.koin.domain.error.dept

import `in`.koreatech.koin.domain.error.KoinErrorException

/**
 * Exceptions related to dept APIs.
 * Don't add Dept prefix because we using sealed class to group exceptions.
 * Every exceptions should ends with Exception.
 */
sealed class KoinDeptException : KoinErrorException() {
    /*
     * Exceptions for 404 Not Found
     */
    class DeptNotFoundException : KoinDeptException()
}
