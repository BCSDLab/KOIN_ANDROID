package `in`.koreatech.koin.ui.bus.adpater

import `in`.koreatech.koin.ui.bus.BusMainFragment
import `in`.koreatech.koin.ui.bus.BusTimeTableFragment
import `in`.koreatech.koin.ui.bus.BusTimeTableSearchFragment
import `in`.koreatech.koin.ui.bus.fragment.BusMainFragmentNew
import android.util.SparseArray
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class BusMainViewPager2Adapter(
    fragmentActivity: FragmentActivity
): FragmentStateAdapter(fragmentActivity) {

    private val registeredFragments = SparseArray<Fragment>()

    override fun getItemCount() = 3

    override fun createFragment(position: Int): Fragment {
        return registeredFragments[position] ?: when(position) {
            0 -> BusMainFragmentNew()
            1 -> BusTimeTableSearchFragment()
            2 -> BusTimeTableFragment()
            else -> throw IllegalArgumentException("Position must be lower than $itemCount")
        }.also { registeredFragments[position] = it }
    }
}