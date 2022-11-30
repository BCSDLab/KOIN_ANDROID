package `in`.koreatech.koin.core.util

import android.os.Looper

val isMainThread get() =  Looper.myLooper() == Looper.getMainLooper()