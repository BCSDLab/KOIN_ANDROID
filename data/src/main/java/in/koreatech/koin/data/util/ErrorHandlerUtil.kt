package `in`.koreatech.koin.data.util

import `in`.koreatech.koin.data.R
import `in`.koreatech.koin.domain.model.error.ErrorHandler
import android.content.Context
import android.util.Log
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

fun Throwable.handleCommonError(context: Context, handleRestError: (Throwable) -> ErrorHandler): ErrorHandler = when (this) {
    is HttpException -> {
        when (this.code()) {
            500 -> ErrorHandler(context.getString(R.string.error_internal_server_error))
            else -> handleRestError(this)
        }
    }
    is ConnectException -> ErrorHandler(context.getString(R.string.error_network_connection))
    is SocketTimeoutException -> ErrorHandler(context.getString(R.string.error_network_connection))
    is UnknownHostException -> ErrorHandler(context.getString(R.string.error_network_connection))
    else -> handleRestError(this)
}.also {
    Log.e("ErrorHandler", stackTraceToString())
}

fun ErrorHandler?.withUnknown(context: Context): ErrorHandler = this ?: unknownErrorHandler(context)
fun unknownErrorHandler(context: Context) = ErrorHandler(context.getString(R.string.error_network_unknown))