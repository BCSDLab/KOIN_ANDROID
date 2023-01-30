package `in`.koreatech.koin.data.error

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import `in`.koreatech.koin.data.util.handleCommonError
import `in`.koreatech.koin.domain.error.bus.BusErrorHandler
import `in`.koreatech.koin.domain.model.Result
import javax.inject.Inject

class BusErrorHandlerImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : BusErrorHandler {
    override fun handleGetBusCoursesError(throwable: Throwable): Result.Error =
        throwable.handleCommonError(context)

    override fun handleGetBusTimetableError(throwable: Throwable): Result.Error =
        throwable.handleCommonError(context)

    override fun handleGetBusRemainTimeError(throwable: Throwable): Result.Error =
        throwable.handleCommonError(context)

    override fun handleGetBusCoursesErrorHandler(throwable: Throwable): Result.Error =
        throwable.handleCommonError(context)

    override fun handleSearchBusError(throwable: Throwable): Result.Error =
        throwable.handleCommonError(context)
}