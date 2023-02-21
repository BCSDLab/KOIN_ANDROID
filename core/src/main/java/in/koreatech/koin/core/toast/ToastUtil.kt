package `in`.koreatech.koin.core.toast

import android.content.Context
import android.widget.Toast

object ToastUtil {
    lateinit var applicationContext: Context

    @Deprecated("Access ToastUtil functions directly")
    @JvmStatic
    val instance = this

    fun init(context: Context) {
        applicationContext = context.applicationContext
    }

    fun makeShort(message: String?) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }

    fun makeLong(message: String?) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
    }

    fun makeShort(message: Int) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }

    fun makeLong(message: Int) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
    }
}