package `in`.koreatech.koin.ui.bus.adpater.timetable.pager

import `in`.koreatech.koin.ui.bus.fragment.BusMainFragment
import `in`.koreatech.koin.ui.bus.fragment.BusSearchFragment
import `in`.koreatech.koin.ui.bus.fragment.BusTimetableFragment
import android.util.SparseArray
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import `in`.koreatech.koin.domain.model.bus.BusType

class BusMainViewPager2Adapter(
    fragmentActivity: FragmentActivity,
    private val type: String
): FragmentStateAdapter(fragmentActivity) {

    private val registeredFragments = SparseArray<Fragment>()

    override fun getItemCount() = 3

    override fun createFragment(position: Int): Fragment {
        return registeredFragments[position] ?: when(position) {
            0 -> BusMainFragment()
            1 -> BusSearchFragment()
            2 -> BusTimetableFragment.newInstance(type)
            else -> throw IllegalArgumentException("Position must be lower than $itemCount")
        }.also { registeredFragments[position] = it }
    }
}