package `in`.koreatech.koin.data.error

import `in`.koreatech.koin.data.util.handleCommonError
import `in`.koreatech.koin.data.util.unknownErrorHandler
import `in`.koreatech.koin.domain.error.bus.BusErrorHandler
import `in`.koreatech.koin.domain.model.error.ErrorHandler
import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.HttpException
import javax.inject.Inject

class BusErrorHandlerImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : BusErrorHandler {
    override fun handleGetBusCoursesError(throwable: Throwable): ErrorHandler =
        throwable.handleCommonError(context) {
            unknownErrorHandler(context)
        }

    override fun handleGetBusTimetableError(throwable: Throwable): ErrorHandler =
        throwable.handleCommonError(context) {
            unknownErrorHandler(context)
        }

    override fun handleGetBusRemainTimeError(throwable: Throwable): ErrorHandler =
        throwable.handleCommonError(context) {
            unknownErrorHandler(context)
        }

    override fun handleGetBusCoursesErrorHandler(throwable: Throwable): ErrorHandler =
        throwable.handleCommonError(context) {
            unknownErrorHandler(context)
        }

    override fun handleSearchBusError(throwable: Throwable): ErrorHandler =
        throwable.handleCommonError(context) {
            unknownErrorHandler(context)
        }
}