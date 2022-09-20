package `in`.koreatech.koin.domain.error.dept

import `in`.koreatech.koin.domain.model.error.ErrorHandler

interface DeptErrorHandler {
    fun getDeptNameFromDeptCodeError(throwable: Throwable): ErrorHandler
    fun getDeptsError(throwable: Throwable): ErrorHandler
}