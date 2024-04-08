package `in`.koreatech.business.util

import android.widget.Toast
import `in`.koreatech.business.KoinBusinessApplication

fun showMessage(message: String){
        Toast.makeText(KoinBusinessApplication.instance, message, Toast.LENGTH_LONG).show()
}