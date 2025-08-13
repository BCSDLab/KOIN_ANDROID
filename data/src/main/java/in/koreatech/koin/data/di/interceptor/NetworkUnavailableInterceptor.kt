package `in`.koreatech.koin.data.di.interceptor

import `in`.koreatech.koin.domain.error.network.KoinNetworkException
import `in`.koreatech.koin.domain.service.NetworkConnectivityService
import java.io.IOException
import javax.inject.Inject
import okhttp3.Interceptor
import okhttp3.Response

class NetworkUnavailableInterceptor @Inject constructor(
    private val networkConnectivityService: NetworkConnectivityService
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        return try {
            chain.proceed(chain.request())
        } catch (e: IOException) {
            if (networkConnectivityService.isConnected()) {
                throw e
            } else {
                throw IOException(KoinNetworkException.NetworkUnavailableException())
            }
        }
    }
}
