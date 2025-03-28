package `in`.koreatech.koin.util.ext

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

fun <T> LifecycleOwner.observeLiveData(liveData: LiveData<T>, observer: Observer<in T>) {
    liveData.observe(this, observer)
}
