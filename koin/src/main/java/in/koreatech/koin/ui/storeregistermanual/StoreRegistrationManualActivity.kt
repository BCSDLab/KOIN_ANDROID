package `in`.koreatech.koin.ui.storeregistermanual

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import `in`.koreatech.koin.R
import `in`.koreatech.koin.constant.StoreRegistrationManual
import `in`.koreatech.koin.databinding.ActivityStoreRegistrationManualBinding

class StoreRegistrationManualActivity : AppCompatActivity() {
    private lateinit var viewPager: ViewPager2
    lateinit var binding: ActivityStoreRegistrationManualBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_store_registration_manual)
        viewPager = binding.viewPager
        val viewPagerAdapter = ManualPagerAdapter(this)
        viewPager.adapter = viewPagerAdapter
    }

    private inner class ManualPagerAdapter(activity: StoreRegistrationManualActivity) : FragmentStateAdapter(activity) {
        override fun getItemCount(): Int = StoreRegistrationManual.PAGE_COUNT

        override fun createFragment(position: Int): Fragment {
            val fragment = StoreRegistrationManualFragment()
            fragment.arguments = Bundle().apply {
                putInt(StoreRegistrationManual.FRAGMENT_PAGE_KEY, position + 1)
            }
            return fragment
        }

    }
}