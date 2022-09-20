package `in`.koreatech.koin.util.ext

import android.widget.EditText

val EditText.textString get() = this.text.toString()

fun EditText.setTransparentBackground() {
    this.background = null
}

fun EditText.setDefaultBackground() {
    val typedArray = context.theme.obtainStyledAttributes(intArrayOf(android.R.attr.editTextBackground))
    this.background = typedArray.getDrawable(0)
}