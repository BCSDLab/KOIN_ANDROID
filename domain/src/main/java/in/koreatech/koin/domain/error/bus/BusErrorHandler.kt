package `in`.koreatech.koin.domain.error.bus

import `in`.koreatech.koin.domain.model.bus.*
import `in`.koreatech.koin.domain.model.error.ErrorHandler
import `in`.koreatech.koin.domain.model.Result

interface BusErrorHandler {
    fun handleGetBusCoursesError(throwable: Throwable) : Result.Error
    fun handleGetBusTimetableError(throwable: Throwable) : Result.Error
    fun handleGetBusRemainTimeError(throwable: Throwable) : Result.Error
    fun handleGetBusCoursesErrorHandler(throwable: Throwable): Result.Error
    fun handleSearchBusError(throwable: Throwable) : Result.Error
}