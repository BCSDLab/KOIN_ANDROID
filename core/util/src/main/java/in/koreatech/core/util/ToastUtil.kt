package `in`.koreatech.core.util

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment

fun Context.makeShortToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Context.makeLongToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

fun Context.makeShortToast(@StringRes resId: Int) {
    Toast.makeText(this, resId, Toast.LENGTH_SHORT).show()
}

fun Context.makeLongToast(@StringRes resId: Int) {
    Toast.makeText(this, resId, Toast.LENGTH_LONG).show()
}

fun Fragment.makeShortToast(message: String) {
    requireContext().makeShortToast(message)
}

fun Fragment.makeLongToast(message: String) {
    requireContext().makeLongToast(message)
}

fun Fragment.makeShortToast(@StringRes resId: Int) {
    requireContext().makeShortToast(resId)
}

fun Fragment.makeLongToast(@StringRes resId: Int) {
    requireContext().makeLongToast(resId)
}