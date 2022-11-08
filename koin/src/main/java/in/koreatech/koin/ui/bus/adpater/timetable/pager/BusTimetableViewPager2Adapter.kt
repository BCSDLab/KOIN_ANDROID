package `in`.koreatech.koin.ui.bus.adpater.timetable.pager

import `in`.koreatech.koin.ui.bus.fragment.*
import android.util.SparseArray
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class BusTimetableViewPager2Adapter(
    fragmentActivity: FragmentActivity
): FragmentStateAdapter(fragmentActivity) {

    private val registeredFragments = SparseArray<Fragment>()

    override fun getItemCount() = 3

    override fun createFragment(position: Int): Fragment {
        return registeredFragments[position] ?: when(position) {
            0 -> ShuttleBusTimetableFragment()
            1 -> ExpressBusTimetableFragment()
            2 -> CityBusTimetableFragment()
            else -> throw IllegalArgumentException("Position must be lower than $itemCount")
        }.also { registeredFragments[position] = it }
    }
}