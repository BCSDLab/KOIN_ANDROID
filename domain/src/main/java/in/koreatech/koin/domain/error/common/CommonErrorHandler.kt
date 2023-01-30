package `in`.koreatech.koin.domain.error.common
import `in`.koreatech.koin.domain.model.Result

interface CommonErrorHandler {
    fun handleError(throwable: Throwable): Result.Error
}