package `in`.koreatech.koin.core.util

import android.app.Activity
import android.os.Looper
import android.view.View
import android.view.ViewGroup
import androidx.activity.ComponentActivity
import androidx.annotation.LayoutRes
import androidx.annotation.MainThread
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class ActivityDataBindingProperty<T : ViewDataBinding>(
    @LayoutRes private val layoutId: Int,
    private val rootView: ViewGroup? = null
) :
    ReadOnlyProperty<AppCompatActivity, T> {

    private var binding: T? = null

    override fun getValue(thisRef: AppCompatActivity, property: KProperty<*>): T {
        if (!isMainThread) {
            throw IllegalAccessException("You should call data binding property delegate only on main thread")
        }

        binding?.let { return it }

        thisRef.lifecycle.addObserver(BindingLifeCycleObserver())
        return DataBindingUtil.inflate<T>(
            thisRef.layoutInflater,
            layoutId,
            rootView ?: thisRef.findViewById(android.R.id.content),
            false
        ).also { binding = it }
    }

    private inner class BindingLifeCycleObserver : DefaultLifecycleObserver {

        @MainThread
        override fun onDestroy(owner: LifecycleOwner) {
            super.onDestroy(owner)
            binding = null
            owner.lifecycle.removeObserver(this)
        }
    }
}

@Suppress("unused")
inline fun <reified T : ViewDataBinding> AppCompatActivity.dataBinding(@LayoutRes layoutId: Int):
        ReadOnlyProperty<AppCompatActivity, T> {
    return ActivityDataBindingProperty(layoutId)
}