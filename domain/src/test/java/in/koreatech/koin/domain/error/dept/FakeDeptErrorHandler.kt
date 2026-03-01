package `in`.koreatech.koin.domain.error.dept

import `in`.koreatech.koin.domain.model.error.ErrorHandler

class FakeDeptErrorHandler : DeptErrorHandler {
    override fun getDeptNameFromDeptCodeError(throwable: Throwable): ErrorHandler {
        return ErrorHandler(throwable.message ?: "Unknown error")
    }

    override fun getDeptsError(throwable: Throwable): ErrorHandler {
        return ErrorHandler(throwable.message ?: "Unknown error")
    }
}
