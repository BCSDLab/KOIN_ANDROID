package `in`.koreatech.koin.ui.dining.adapter

import android.util.SparseArray
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import `in`.koreatech.koin.domain.constant.BREAKFAST
import `in`.koreatech.koin.domain.constant.DINNER
import `in`.koreatech.koin.domain.constant.LUNCH
import `in`.koreatech.koin.ui.dining.DiningItemsFragment

class DiningItemsViewPager2Adapter(
    fragmentActivity: FragmentActivity
) : FragmentStateAdapter(fragmentActivity) {

    private val fragments = SparseArray<Fragment>()

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return fragments[position] ?: when(position) {
            0 -> DiningItemsFragment.newInstance(BREAKFAST)
            1 -> DiningItemsFragment.newInstance(LUNCH)
            2 -> DiningItemsFragment.newInstance(DINNER)
            else -> Fragment()
        }.also { fragments[position] = it }
    }
}