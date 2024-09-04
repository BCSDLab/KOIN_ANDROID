package `in`.koreatech.koin.core.viewpager

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.viewpager2.widget.ViewPager2

fun ViewPager2.enableAutoScroll(lifecycleOwner: LifecycleOwner, interval: Long) {

    val runnable = Runnable {
        if (currentItem < (adapter?.itemCount ?: 0) - 1) {
            setCurrentItem(currentItem + 1, true)
        } else {
            setCurrentItem(0, true)
        }
    }

    val callback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            removeCallbacks(runnable)
            postDelayed(runnable, interval)
        }
    }

    lifecycleOwner.lifecycle.addObserver(object : DefaultLifecycleObserver {
        override fun onResume(owner: LifecycleOwner) {
            postDelayed(runnable, interval)
            registerOnPageChangeCallback(callback)
        }
        override fun onPause(owner: LifecycleOwner) {
            removeCallbacks(runnable)
            unregisterOnPageChangeCallback(callback)
        }
    })
}