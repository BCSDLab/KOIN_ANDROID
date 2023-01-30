package `in`.koreatech.koin.domain.error.dept

import `in`.koreatech.koin.domain.model.Result

interface DeptErrorHandler {
    fun getDeptNameFromDeptCodeError(throwable: Throwable): Result.Error
    fun getDeptsError(throwable: Throwable): Result.Error
}