package `in`.koreatech.koin.core.util

import android.util.Log
import androidx.annotation.MainThread
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class FragmentDataBindingProperty<T : ViewDataBinding> :
    ReadOnlyProperty<Fragment, T> {

    private var binding: T? = null

    override fun getValue(thisRef: Fragment, property: KProperty<*>): T {
        if (!isMainThread) {
            throw IllegalAccessException("You should call data binding property delegate only on main thread")
        }

        if(binding != null) return binding!!

        thisRef.viewLifecycleOwner.lifecycle.addObserver(BindingLifeCycleObserver())

        val view = checkNotNull(thisRef.view) {
            throw IllegalAccessException("Fragment binding property can be called after onCreateView.")
        }

        binding = DataBindingUtil.bind(view)

        return checkNotNull(binding) {
            throw IllegalAccessException("Fragment binding property can be called after onCreateView.")
        }
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
inline fun <reified T : ViewDataBinding> Fragment.dataBinding():
        ReadOnlyProperty<Fragment, T> {
    return FragmentDataBindingProperty()
}