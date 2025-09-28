package `in`.koreatech.koin.data.di.interceptor

import `in`.koreatech.koin.domain.error.network.NetworkUnavailableException
import java.io.IOException
import okhttp3.Interceptor
import okhttp3.Response

class NetworkUnavailableInterceptor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        return try {
            chain.proceed(chain.request())
        } catch (_: IOException) {
            throw NetworkUnavailableException()
        }
    }
}
