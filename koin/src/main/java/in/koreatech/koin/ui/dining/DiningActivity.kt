package `in`.koreatech.koin.ui.dining

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import `in`.koreatech.koin.R
import `in`.koreatech.koin.core.analytics.EventLogger
import `in`.koreatech.koin.core.appbar.AppBarBase
import `in`.koreatech.koin.core.constant.AnalyticsConstant
import `in`.koreatech.koin.core.util.dataBinding
import `in`.koreatech.koin.databinding.ActivityDiningBinding
import `in`.koreatech.koin.domain.model.dining.DiningType
import `in`.koreatech.koin.domain.util.DiningUtil
import `in`.koreatech.koin.domain.util.TimeUtil
import `in`.koreatech.koin.ui.dining.adapter.DiningDateAdapter
import `in`.koreatech.koin.ui.dining.adapter.DiningItemsViewPager2Adapter
import `in`.koreatech.koin.ui.dining.viewmodel.DiningViewModel
import `in`.koreatech.koin.ui.navigation.KoinNavigationDrawerActivity
import `in`.koreatech.koin.ui.navigation.state.MenuState
import `in`.koreatech.koin.util.ext.toggleDrawer
import `in`.koreatech.koin.util.ext.withLoading
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.Date

@AndroidEntryPoint
class DiningActivity : KoinNavigationDrawerActivity() {
    override val menuState: MenuState = MenuState.Dining
    val binding: ActivityDiningBinding by dataBinding<ActivityDiningBinding>(R.layout.activity_dining)
    override val screenTitle = "식단"
    private val viewModel by viewModels<DiningViewModel>()
    private val dates = mutableListOf<Date>()
    private val diningDateAdapter by lazy { DiningDateAdapter {
        viewModel.setSelectedDate(it)
    } }
    private lateinit var diningViewPagerScrollCallback: ViewPager2.OnPageChangeCallback
    private var initialDateTab = 0
    private var initialDiningTab = 0
    private val diningOnBoardingBottomSheet by lazy {
        DiningNotificationOnBoardingFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initDiningViewPagerScrollCallback()
        initCalendar()
        initViewPager()
        onActionView()
        selectInitialPositions()

        withLoading(this, viewModel)
        viewModel.getDining()

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.selectedDate.collect {
                    viewModel.getDining(it)
                }
            }
        }

        lifecycleScope.launch {
            viewModel.userState.collect {
                if(it != null && it.isAnonymous.not()) {
                    viewModel.getShouldShowNotificationOnBoarding()
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.showDiningNotificationOnBoarding.collect { shouldShowOnBoarding ->
                    if (shouldShowOnBoarding) {
                        diningOnBoardingBottomSheet.show(
                            supportFragmentManager,
                            diningOnBoardingBottomSheet.tag
                        )
                        viewModel.updateShouldShowNotificationOnBoarding(false)
                    }
                }
            }
        }

        binding.koinBaseAppBarDark.setOnClickListener {
            when(it.id) {
                AppBarBase.getLeftButtonId() -> onBackPressed()
                AppBarBase.getRightButtonId() -> binding.drawerLayout.toggleDrawer()
            }
        }
    }

    private fun selectInitialPositions() {
        binding.tabsDiningTime.selectTab(binding.tabsDiningTime.getTabAt(initialDiningTab))
        diningDateAdapter.selectPosition(initialDateTab)
    }

    private fun onActionView() {
        if(Intent.ACTION_VIEW == intent.action) {
            val uri = intent.data
            uri?.let {
                it.getQueryParameter("type")?.let { type ->
                    initialDiningTab = getDiningTabByType(DiningUtil.getTypeByString(type))

                }
                val dateString = it.getQueryParameter("date")
                dateString?.let { ds ->
                    val date = TimeUtil.stringToDateYYYYMMDD(ds)
                    val diff = TimeUtil.getDateDifferenceWithToday(date)
                    initialDateTab = dates.size / 2 + diff
                }
            }
        }
    }

    private fun initViewPager() {
        with(binding) {
            diningViewPager.apply {
                offscreenPageLimit = 3
                adapter = DiningItemsViewPager2Adapter(this@DiningActivity)
                registerOnPageChangeCallback(diningViewPagerScrollCallback)
            }
            TabLayoutMediator(tabsDiningTime, diningViewPager) { tab, position ->
                tab.text = when (position) {
                    0 -> getString(R.string.dining_breakfast)
                    1 -> getString(R.string.dining_lunch)
                    2 -> getString(R.string.dining_dinner)
                    else -> throw IllegalArgumentException("Position must be lower than ${diningViewPager.offscreenPageLimit}")
                }
            }.attach()

            initialDiningTab = getDiningTabByType(DiningUtil.getCurrentType())

            // 스크롤이 아닌 탭 선택 이벤트만 받기 위한 구현
            repeat(binding.tabsDiningTime.tabCount) {
                val tab = binding.tabsDiningTime.getTabAt(it)
                tab?.view?.setOnClickListener {
                    EventLogger.logClickEvent(
                        AnalyticsConstant.Domain.CAMPUS,
                        AnalyticsConstant.Label.MENU_TIME,
                        tab.text.toString()
                    )
                }
            }
        }
    }

    private fun initCalendar() {
        with(binding) {
            recyclerViewCalendar.adapter = diningDateAdapter
            val current = TimeUtil.getCurrentTime()
            dates.add(current)
            Log.d("DiningActivity", "initCalendar: $current")
            repeat(3) {
                dates.add(0, TimeUtil.getPreviousDayDate(dates.first()))
            }
            repeat(3) {
                dates.add(TimeUtil.getNextDayDate(dates.last()))
            }
            diningDateAdapter.submitList(dates)

            val todayPos = dates.size / 2
            scrollDateTodayToCenter(todayPos)
            initialDateTab = todayPos
        }
    }

    private fun scrollDateTodayToCenter(todayPosition: Int) {
        val layoutManager = binding.recyclerViewCalendar.layoutManager as? LinearLayoutManager
        val screenWidthPx = resources.displayMetrics.widthPixels
        binding.recyclerViewCalendar.post {
            val itemWidthPx = binding.recyclerViewCalendar.getChildAt(0).width
            val offset = (screenWidthPx / 2 - itemWidthPx / 2)
            layoutManager?.scrollToPositionWithOffset(todayPosition, offset)
        }
    }

    private fun initDiningViewPagerScrollCallback() {
        // 탭 선택이 아닌 스크롤 이벤트만 받기 위한 구현
        diningViewPagerScrollCallback = object : ViewPager2.OnPageChangeCallback() {
            var isUserScrolling = false
            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
                if(state == ViewPager2.SCROLL_STATE_DRAGGING){
                    isUserScrolling = true
                } else if(state == ViewPager2.SCROLL_STATE_IDLE){
                    if(isUserScrolling){
                        EventLogger.logScrollEvent(
                            AnalyticsConstant.Domain.CAMPUS,
                            AnalyticsConstant.Label.MENU_TIME,
                            binding.tabsDiningTime.getTabAt(binding.tabsDiningTime.selectedTabPosition)?.text.toString()
                        )
                    }
                    isUserScrolling = false
                }
            }
        }
    }

    private fun getDiningTabByType(type: DiningType): Int {
        return when (type) {
            DiningType.Breakfast -> 0
            DiningType.Lunch -> 1
            DiningType.Dinner -> 2
            DiningType.NextBreakfast -> 0
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.diningViewPager.unregisterOnPageChangeCallback(diningViewPagerScrollCallback)
    }
}