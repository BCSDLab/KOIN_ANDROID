package `in`.koreatech.koin.ui.store.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import `in`.koreatech.koin.ui.store.fragment.StoreEventFragment
import `in`.koreatech.koin.ui.store.fragment.StoreMenuFragment

class StoreDetailViewpagerAdapter(fragment: FragmentActivity): FragmentStateAdapter(fragment){
    override fun getItemCount() = PAGE_NUMBER
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> StoreMenuFragment()
            1 -> StoreEventFragment()
            else -> throw IndexOutOfBoundsException()
        }
    }
    companion object {
        const val PAGE_NUMBER = 2

    }
}