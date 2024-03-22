package `in`.koreatech.koin.util.ext

import android.content.Context
import android.widget.Toast

fun Context.shortToast(toastMessage: String) = Toast.makeText(this, toastMessage, Toast.LENGTH_SHORT).show()