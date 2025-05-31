package `in`.koreatech.business.di.userAgent

import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class UserAgentInterceptor @Inject constructor(
    private val userAgentProvider: UserAgentProvider
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        if (originalRequest.header("User-Agent").isNullOrEmpty()) {
            val newRequest = originalRequest.newBuilder()
                .header("User-Agent", userAgentProvider.getUserAgent())
                .build()
            return chain.proceed(newRequest)
        }
        return chain.proceed(originalRequest)
    }
}