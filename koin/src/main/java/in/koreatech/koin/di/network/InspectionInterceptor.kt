package `in`.koreatech.koin.di.network

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import `in`.koreatech.koin.ui.error.InspectionActivity
import okhttp3.Interceptor
import okhttp3.Response

class InspectionInterceptor(
    private val context: Context
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())
        if (response.code == 503) {
            val intent = Intent(context, InspectionActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
            }
            Handler(Looper.getMainLooper()).post {
                context.startActivity(intent)
            }
        }
        return response
    }
}
