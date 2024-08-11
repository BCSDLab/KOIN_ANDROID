package `in`.koreatech.koin.ui.bus

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import `in`.koreatech.koin.R
import `in`.koreatech.koin.core.appbar.AppBarBase
import `in`.koreatech.koin.core.util.FontManager
import `in`.koreatech.koin.core.util.dataBinding
import `in`.koreatech.koin.databinding.BusActivityMainBinding
import `in`.koreatech.koin.domain.model.bus.BusType
import `in`.koreatech.koin.ui.bus.adpater.timetable.pager.BusMainViewPager2Adapter
import `in`.koreatech.koin.ui.navigation.KoinNavigationDrawerActivity
import `in`.koreatech.koin.ui.navigation.state.MenuState
import `in`.koreatech.koin.util.FirebasePerformanceUtil
import `in`.koreatech.koin.util.ext.hideSoftKeyboard

@AndroidEntryPoint
class BusActivity : KoinNavigationDrawerActivity() {
    private val binding by dataBinding<BusActivityMainBinding>(R.layout.bus_activity_main)
    override val screenTitle = "버스"
    private lateinit var busMainViewPager2Adapter : BusMainViewPager2Adapter
    private val firebasePerformanceUtil by lazy {
        FirebasePerformanceUtil("Bus_Activity")
    }
    override val menuState: MenuState = MenuState.Bus

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        firebasePerformanceUtil.start()

        initView()
    }

    private fun initView() = with(binding) {
        val tabStartPosition = intent.getIntExtra("tab", 0)
        val timetableMenu = intent.getStringExtra("timetableMenu") ?: BusType.Shuttle.busTypeString

        busMainViewPager2Adapter = BusMainViewPager2Adapter(this@BusActivity, timetableMenu)

        koinBaseAppbar.setOnClickListener {
            when (it.id) {
                AppBarBase.getLeftButtonId() -> callDrawerItem(R.id.navi_item_home)
                AppBarBase.getRightButtonId() -> toggleNavigationDrawer()
            }
        }
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
        if (tabStartPosition != 0) {
            busMainTabs.getTabAt(tabStartPosition)?.select()
            busMainViewpager.currentItem = tabStartPosition
        }
        changeFont(busMainTabs.getChildAt(0))
    }

    override fun onStart() {
        super.onStart()
        binding.busMainViewpager.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
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
                viewChild.typeface = FontManager.getTypeface(this, FontManager.KoinFontType.PRETENDARD_REGULAR)
            } else (viewChild as? ViewGroup)?.let { changeFont(it) }
        }
    }

    override fun onDestroy() {
        firebasePerformanceUtil.stop()
        super.onDestroy()
    }
}