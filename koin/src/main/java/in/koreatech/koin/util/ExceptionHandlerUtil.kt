package `in`.koreatech.koin.util

import android.content.Context
import android.content.Intent
import `in`.koreatech.koin.ui.error.ErrorActivity
import java.io.PrintWriter
import java.io.StringWriter
import java.io.Writer
import kotlin.system.exitProcess

class ExceptionHandlerUtil(private val context: Context) : Thread.UncaughtExceptionHandler {
    /***
     * UncaughtException을 캐치하여 처리하는 함수
     * Error message가 있다면 ErrorActivity로 이동
     * @param thread
     * @param throwable
     */
    override fun uncaughtException(thread: Thread, throwable: Throwable) {
        val stringWriter = StringWriter()
        throwable.printStackTrace(PrintWriter(stringWriter as Writer)) //오류 메시지를 얻는다.
        val errorMessage = stringWriter.toString()
        startErrorActivity(context, errorMessage)
        exitProcess(-1) //가장 위에 있는 액티비티를 종료 finish()와 같다.
    }

    /***
     * ErrorActivity로 이동하는 함수
     * Error message도 전달
     * @param context
     * @param ErrorMessage
     */
    private fun startErrorActivity(context: Context, ErrorMessage: String) {
        val goToErrorActivityIntent = Intent(context.applicationContext, ErrorActivity::class.java)
        goToErrorActivityIntent.putExtra(EXTRA_ERROR_TEXT, ErrorMessage)
        goToErrorActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        context.startActivity(goToErrorActivityIntent)
    }

    companion object {
        const val EXTRA_ERROR_TEXT = "EXTRA_ERROR_TEXT"
    }
}