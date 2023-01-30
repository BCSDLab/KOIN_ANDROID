package `in`.koreatech.koin.data.util

import `in`.koreatech.koin.data.R
import `in`.koreatech.koin.domain.model.error.ErrorHandler
import android.content.Context
import android.util.Log
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

import `in`.koreatech.koin.domain.model.Result

fun Throwable.handleCommonError(
    context: Context,
    handleRestError: ((Throwable) -> Result.Error?)? = null
): Result.Error = when (this) {
    is HttpException -> {
        when (this.code()) {
            500 -> Result.Error(context.getString(R.string.error_internal_server_error))
            else -> if (handleRestError == null) unknownError(context) else {
                handleRestError(this)
                    ?: unknownError(context)
            }
        }
    }
    is ConnectException -> Result.Error(context.getString(R.string.error_network_connection))
    is SocketTimeoutException -> Result.Error(context.getString(R.string.error_network_connection))
    is UnknownHostException -> Result.Error(context.getString(R.string.error_network_connection))
    else ->  if (handleRestError == null) unknownError(context) else {
        handleRestError(this)
            ?: unknownError(context)
    }
}.also {
    Log.e("ErrorHandler", stackTraceToString())
}
fun unknownError(context: Context) = Result.Error(context.getString(R.string.error_unknown))