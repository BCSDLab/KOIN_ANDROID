package `in`.koreatech.koin.util.ext

import android.app.Activity
import androidx.activity.ComponentActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

fun <T> LifecycleOwner.observeLiveData(liveData: LiveData<T>, observer: Observer<in T>) {
    liveData.observe(this, observer)
}