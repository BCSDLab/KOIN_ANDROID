package `in`.koreatech.business.di.userAgent

import android.content.Context
import okhttp3.Interceptor
import okhttp3.Response

class InspectionInterceptor(
    private val context: Context
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())

        return response
    }
}
