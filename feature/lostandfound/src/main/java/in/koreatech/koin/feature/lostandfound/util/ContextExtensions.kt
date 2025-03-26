package `in`.koreatech.koin.feature.lostandfound.util

import android.content.Context
import android.content.ContextWrapper
import androidx.activity.ComponentActivity

internal fun Context.findActivity(): ComponentActivity? =
    when (this) {
        is ComponentActivity -> this
        is ContextWrapper -> baseContext.findActivity()
        else -> null
    }
