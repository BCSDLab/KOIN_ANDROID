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
        binding.goRegisterButton.setOnClickListener {
            //액티비티 이동
        }
    }

    private inner class ManualPagerAdapter(activity: StoreRegistrationManualActivity) : FragmentStateAdapter(activity) {
        override fun getItemCount(): Int = StoreRegistrationManual.PAGE_COUNT

        override fun createFragment(position: Int): Fragment {
            val fragment = StoreRegistrationManualFragment()
            fragment.arguments = Bundle().apply {
                putInt(StoreRegistrationManual.FRAGMENT_PAGE_KEY, position + 1)
            }

            when (position) {
                StoreRegistrationManual.PAGE_COUNT - 1 -> {
                    binding.dotsImageView.visibility = View.GONE
                    binding.goRegisterButton.visibility = View.VISIBLE
                }
                0 -> {
                    binding.dotsImageView.visibility = View.VISIBLE
                    binding.goRegisterButton.visibility = View.GONE
                    binding.dotsImageView.setImageResource(R.drawable.store_registration_manual_page_1)
                }
                1 -> {
                    binding.dotsImageView.visibility = View.VISIBLE
                    binding.goRegisterButton.visibility = View.GONE
                    binding.dotsImageView.setImageResource(R.drawable.store_registration_manual_page_2)
                }
                3 -> {
                    binding.dotsImageView.visibility = View.VISIBLE
                    binding.goRegisterButton.visibility = View.GONE
                    binding.dotsImageView.setImageResource(R.drawable.store_registration_manual_page_3)
                }
            }
            return fragment
        }

    }
}