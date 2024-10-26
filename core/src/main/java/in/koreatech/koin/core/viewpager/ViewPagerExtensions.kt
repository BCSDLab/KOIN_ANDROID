package `in`.koreatech.koin.core.viewpager

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback

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

/**
 * 페이지가 스크롤되었을 때 호출, (TabLayout 연동 시 탭 클릭에 의한 스크롤에는 호출 X)
 */
fun ViewPager2.addOnPageScrollListener(lifecycleOwner: LifecycleOwner, action: (Int) -> Unit) {
    val callback = object : OnPageChangeCallback() {
        var isUserScrolling = false
        override fun onPageScrollStateChanged(state: Int) {
            super.onPageScrollStateChanged(state)
            if (state == ViewPager2.SCROLL_STATE_DRAGGING) {
                isUserScrolling = true
            } else if (state == ViewPager2.SCROLL_STATE_IDLE) {
                if (isUserScrolling) {
                    action(currentItem)
                }
                isUserScrolling = false
            }
        }
    }
    addOnPageChangedListener(lifecycleOwner, callback)
}

fun ViewPager2.addOnPageChangedListener(lifecycleOwner: LifecycleOwner, onPageChangeCallback: OnPageChangeCallback) {
    lifecycleOwner.lifecycle.addObserver(object : DefaultLifecycleObserver {
        override fun onResume(owner: LifecycleOwner) {
            registerOnPageChangeCallback(onPageChangeCallback)
        }

        override fun onPause(owner: LifecycleOwner) {
            unregisterOnPageChangeCallback(onPageChangeCallback)
        }
    })
}