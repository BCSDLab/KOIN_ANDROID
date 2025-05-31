package `in`.koreatech.business.di.userAgent

import javax.inject.Inject
import okhttp3.Interceptor
import okhttp3.Response

class UserAgentInterceptor @Inject constructor(
    private val userAgentProvider: UserAgentProvider
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        val newRequest = originalRequest.newBuilder()
            .removeHeader("User-Agent")
            .header("User-Agent", userAgentProvider.getUserAgent())
            .build()
        return chain.proceed(newRequest)

        return chain.proceed(originalRequest)
    }
}
