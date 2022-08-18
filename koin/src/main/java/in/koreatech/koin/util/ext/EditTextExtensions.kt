package `in`.koreatech.koin.util.ext

import android.widget.EditText

val EditText.textString get() = this.text.toString()