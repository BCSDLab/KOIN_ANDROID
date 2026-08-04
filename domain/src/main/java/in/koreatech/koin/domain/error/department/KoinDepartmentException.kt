package `in`.koreatech.koin.domain.error.department

import `in`.koreatech.koin.domain.error.KoinErrorException

sealed class KoinDepartmentException : KoinErrorException() {
    class DepartmentNotFoundException : KoinDepartmentException()
}
