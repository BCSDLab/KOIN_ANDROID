package `in`.koreatech.koin.data.mapper

import `in`.koreatech.koin.domain.error.LoginError.LoginError
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

fun Throwable.ThrowableMapper(): Result<Unit> {
    return when (this) {
        is HttpException -> {
            when (this.code()) {
                400 -> Result.failure(LoginError.IncorrectIdPwError)
                else -> Result.failure(LoginError.NetworkError)
            }
        }
        is ConnectException -> Result.failure(LoginError.NetworkError)
        is SocketTimeoutException -> Result.failure(LoginError.NetworkError)
        is UnknownHostException -> Result.failure(LoginError.NetworkError)
        else -> Result.failure(LoginError.UnknownError)
    }
}