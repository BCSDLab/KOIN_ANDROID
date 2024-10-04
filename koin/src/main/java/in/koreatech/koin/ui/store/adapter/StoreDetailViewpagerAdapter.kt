package `in`.koreatech.koin.ui.store.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import `in`.koreatech.koin.ui.store.fragment.StoreDetailEventFragment
import `in`.koreatech.koin.ui.store.fragment.StoreDetailMenuFragment
import `in`.koreatech.koin.ui.store.fragment.StoreDetailReviewFragment

class StoreDetailViewpagerAdapter(fragment: FragmentActivity): FragmentStateAdapter(fragment){
    override fun getItemCount() = PAGE_NUMBER
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> StoreDetailMenuFragment()
            1 -> StoreDetailEventFragment()
            2 -> StoreDetailReviewFragment()
            else -> throw IndexOutOfBoundsException()
        }
    }
    companion object {
        const val PAGE_NUMBER = 3

    }
}