package `in`.koreatech.koin.util.ext

import `in`.koreatech.koin.core.activity.ActivityBase
import `in`.koreatech.koin.core.progressdialog.IProgressDialog
import `in`.koreatech.koin.core.viewmodel.BaseViewModel
import android.app.Activity
import android.content.Context
import android.os.Build
import android.util.DisplayMetrics
import android.view.WindowInsets
import android.view.inputmethod.InputMethodManager
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.LifecycleOwner
import java.util.*
import kotlin.math.roundToInt


inline val Activity.windowHeight: Int
    get() {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val metrics = windowManager.currentWindowMetrics
            val insets = metrics.windowInsets.getInsets(WindowInsets.Type.systemBars())
            metrics.bounds.height() - insets.bottom - insets.top
        } else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val view = window.decorView
            val insets = WindowInsetsCompat.toWindowInsetsCompat(view.rootWindowInsets, view).getInsets(WindowInsetsCompat.Type.systemBars())
            resources.displayMetrics.heightPixels - insets.bottom - insets.top
        } else {
            DisplayMetrics().let { displayMetrics ->
                windowManager.defaultDisplay.getMetrics(displayMetrics)
                displayMetrics.heightPixels
            }
        }
    }

inline val Activity.windowWidth: Int
    get() {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val metrics = windowManager.currentWindowMetrics
            val insets = metrics.windowInsets.getInsets(WindowInsets.Type.systemBars())
            metrics.bounds.width() - insets.left - insets.right
        } else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val view = window.decorView
            val insets = WindowInsetsCompat.toWindowInsetsCompat(view.rootWindowInsets, view).getInsets(WindowInsetsCompat.Type.systemBars())
            resources.displayMetrics.widthPixels - insets.left - insets.right
        } else {
            DisplayMetrics().let { displayMetrics ->
                windowManager.defaultDisplay.getMetrics(displayMetrics)
                displayMetrics.widthPixels
            }
        }
    }

fun <T : BaseViewModel> IProgressDialog.withLoading(lifecycleOwner: LifecycleOwner, viewModel: T) {
    viewModel.isLoading.observe(lifecycleOwner) {
        if(it) {
            showProgressDialog("로딩 중...")
        } else {
            hideProgressDialog()
        }
    }
}

fun Activity.showSoftKeyboard() {
    val imm = getSystemService(ActivityBase.INPUT_METHOD_SERVICE) as InputMethodManager
    Objects.requireNonNull(imm).showSoftInput(currentFocus, 0)
}

fun Activity.hideSoftKeyboard() {
    val imm = getSystemService(ActivityBase.INPUT_METHOD_SERVICE) as InputMethodManager
    Objects.requireNonNull(imm).hideSoftInputFromWindow(currentFocus?.windowToken, 0)
}

fun Activity.dpToPx(dp: Int): Int {
    val density: Float = resources.displayMetrics.density
    return (dp.toFloat() * density).roundToInt()
}