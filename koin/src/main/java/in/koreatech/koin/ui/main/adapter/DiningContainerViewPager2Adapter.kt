package `in`.koreatech.koin.ui.main.adapter

import android.util.SparseArray
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import `in`.koreatech.koin.domain.model.dining.DiningPlace
import `in`.koreatech.koin.ui.main.fragment.DiningContainerFragment

class DiningContainerViewPager2Adapter(
    fragmentActivity: FragmentActivity
) : FragmentStateAdapter(fragmentActivity) {
    private val fragments = SparseArray<Fragment>()

    override fun getItemCount(): Int = 4

    override fun createFragment(position: Int): Fragment {
        return fragments[position] ?: when (position) {
            0 -> DiningContainerFragment.newInstance(DiningPlace.CornerA.place)
            1 -> DiningContainerFragment.newInstance(DiningPlace.CornerB.place)
            2 -> DiningContainerFragment.newInstance(DiningPlace.CornerC.place)
            3 -> DiningContainerFragment.newInstance(DiningPlace.Nungsu.place)
            else -> throw IllegalArgumentException("Position must be lower than $itemCount")
        }.also { fragments[position] = it }
    }
}