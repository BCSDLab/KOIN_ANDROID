package `in`.koreatech.koin.core.util

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

class KeyboardUtils(
    private val context: Context
) {
    // 키보드 올리기
    fun show(view: View) {
        val inputMethodManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        view.requestFocus()
        inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }

    // 키보드 내리기
    fun hide(view: View) {
        val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        view.clearFocus()
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, InputMethodManager.HIDE_IMPLICIT_ONLY)
    }
}
