package `in`.koreatech.koin.domain.error.bus

import `in`.koreatech.koin.domain.model.bus.*
import `in`.koreatech.koin.domain.model.error.ErrorHandler

interface BusErrorHandler {
    fun handleGetBusCoursesError(throwable: Throwable) : ErrorHandler
    fun handleGetBusTimetableError(throwable: Throwable) : ErrorHandler
    fun handleGetBusRemainTimeError(throwable: Throwable) : ErrorHandler
    fun handleSearchBusError(throwable: Throwable) : ErrorHandler
}