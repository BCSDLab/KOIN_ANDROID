package `in`.koreatech.koin.domain.error.recruitment

import `in`.koreatech.koin.domain.error.KoinErrorException

sealed class KoinRecruitmentException : KoinErrorException() {
    /*
     * Exceptions for 403
     */
    class ForbiddenException : KoinRecruitmentException()
}
