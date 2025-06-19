package `in`.koreatech.business.di.userAgent

import android.content.Context
import android.content.Intent
import `in`.koreatech.business.main.BusinessMainActivity
import okhttp3.Interceptor
import okhttp3.Response

class InspectionInterceptor(
    private val context: Context
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())

        if (response.code == 503) {
            val intent = Intent(context, BusinessMainActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
            }
            context.startActivity(intent)
        }
        return response
    }
}