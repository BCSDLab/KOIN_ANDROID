package `in`.koreatech.koin.ui.bus

import `in`.koreatech.koin.R
import `in`.koreatech.koin.core.activity.ActivityBase
import `in`.koreatech.koin.core.util.dataBinding
import `in`.koreatech.koin.databinding.BusActivityMainBinding
import `in`.koreatech.koin.ui.bus.adpater.BusMainViewPager2Adapter
import `in`.koreatech.koin.util.FirebasePerformanceUtil
import `in`.koreatech.koin.util.ext.hideSoftKeyboard
import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator

class BusActivity : ActivityBase() {
    private val binding by dataBinding<BusActivityMainBinding>(R.layout.bus_activity_main)
    private val busMainViewPager2Adapter = BusMainViewPager2Adapter(this)
    private val firebasePerformanceUtil by lazy {
        FirebasePerformanceUtil("Bus_Activity")
    }

    var departureState = 0 // 0 : 한기대 1 : 야우리 2 : 천안역
    var arrivalState = 1 // 0 : 한기대 1 : 야우리 2 : 천안역

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        firebasePerformanceUtil.start()

        initView()
    }

    private fun initView() = with(binding) {
        busMainViewpager.apply {
            offscreenPageLimit = 3
            adapter = busMainViewPager2Adapter
        }
        TabLayoutMediator(busMainTabs, busMainViewpager) { tab, position ->
            tab.text = when (position) {
                0 -> getString(R.string.bus_tab_info)
                1 -> getString(R.string.bus_tab_search_info)
                2 -> getString(R.string.bus_tab_timetable)
                else -> throw IllegalArgumentException("Position must be lower than ${busMainViewPager2Adapter.itemCount}")
            }
        }.attach()
        changeFont(busMainTabs.getChildAt(0))
    }

    override fun onStart() {
        super.onStart()
        binding.busMainViewpager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
                hideSoftKeyboard()
            }
        })
    }

    private fun changeFont(view: View) {
        val viewGroup = view as ViewGroup
        val childCount = viewGroup.childCount
        for (i in 0 until childCount) {
            val viewChild = viewGroup.getChildAt(i)
            if (viewChild is TextView) {
                viewChild.setTypeface(Typeface.createFromAsset(assets, TABLAYOUT_FONT_NAME))
            } else (viewChild as? ViewGroup)?.let { changeFont(it) }
        }
    }

    override fun onDestroy() {
        firebasePerformanceUtil.stop()
        super.onDestroy()
    }

    companion object {
        private const val TABLAYOUT_FONT_NAME = "fonts/notosanscjkkr_regular.otf"
    }
}