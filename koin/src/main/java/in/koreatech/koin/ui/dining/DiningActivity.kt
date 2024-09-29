package `in`.koreatech.koin.ui.dining

import android.content.Intent
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import `in`.koreatech.koin.R
import `in`.koreatech.koin.core.analytics.EventAction
import `in`.koreatech.koin.core.analytics.EventLogger
import `in`.koreatech.koin.core.appbar.AppBarBase
import `in`.koreatech.koin.core.constant.AnalyticsConstant
import `in`.koreatech.koin.core.util.dataBinding
import `in`.koreatech.koin.core.viewpager.addOnPageScrollListener
import `in`.koreatech.koin.databinding.ActivityDiningBinding
import `in`.koreatech.koin.domain.model.dining.DiningType
import `in`.koreatech.koin.domain.util.DiningUtil
import `in`.koreatech.koin.domain.util.TimeUtil
import `in`.koreatech.koin.ui.dining.adapter.DiningDateAdapter
import `in`.koreatech.koin.ui.dining.adapter.DiningItemsViewPager2Adapter
import `in`.koreatech.koin.ui.dining.viewmodel.DiningViewModel
import `in`.koreatech.koin.ui.main.activity.MainActivity
import `in`.koreatech.koin.ui.navigation.KoinNavigationDrawerActivity
import `in`.koreatech.koin.ui.navigation.state.MenuState
import `in`.koreatech.koin.util.ext.withLoading
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
    private var initialDateTab = 0
    private var initialDiningTab = 0
    private val diningOnBoardingBottomSheet by lazy {
        DiningNotificationOnBoardingFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initCalendar()
        initViewPager()
        onActionView()
        selectInitialPositions()
        initOnRefreshDiningList()

        withLoading(this, viewModel)

        lifecycleScope.launch {
            viewModel.userState.collect {
                if(it != null && it.isAnonymous.not()) {
                    viewModel.shouldShowNotificationOnBoarding()
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
                AppBarBase.getRightButtonId() -> {
                    EventLogger.logClickEvent(
                        EventAction.CAMPUS,
                        AnalyticsConstant.Label.CAFETERIA_INFO,
                        getString(R.string.cafeteria_info)
                    )
                    startActivity(Intent(this@DiningActivity, DiningNoticeActivity::class.java))
                }
            }
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (isTaskRoot) {
                    val intent = Intent(this@DiningActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    isEnabled = false
                    onBackPressedDispatcher.onBackPressed()
                }
            }
        })
    }

    private fun selectInitialPositions() {
        binding.tabsDiningTime.selectTab(binding.tabsDiningTime.getTabAt(initialDiningTab))
        diningDateAdapter.selectPosition(initialDateTab)
        diningDateAdapter.notifyDataSetChanged()
        viewModel.setSelectedDate(dates[initialDateTab])
    }

    private fun onActionView() {
        if(Intent.ACTION_VIEW == intent.action) {
            val uri = intent.data
            uri?.let {
                try {
                    val dateString = it.getQueryParameter("date")
                    dateString?.let { ds ->
                        val date = TimeUtil.stringToDateYYYYMMDD(ds)
                        val diff = TimeUtil.getDateDifferenceInDays(date, dates[dates.size / 2])
                        initialDateTab = dates.size / 2 + diff
                    }

                    it.getQueryParameter("type")?.let { type ->
                        initialDiningTab = getDiningTabByType(DiningUtil.getTypeByString(type))
                    }
                } catch (_: Exception) { }
            }
        }
    }

    private fun initViewPager() {
        with(binding) {
            diningViewPager.apply {
                offscreenPageLimit = 3
                adapter = DiningItemsViewPager2Adapter(this@DiningActivity)
                addOnPageScrollListener(this@DiningActivity) {
                    EventLogger.logScrollEvent(
                        EventAction.CAMPUS,
                        AnalyticsConstant.Label.MENU_TIME,
                        tabsDiningTime.getTabAt(it)?.text.toString()
                    )
                }
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
                        EventAction.CAMPUS,
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
            dates.clear()
            val current = TimeUtil.getCurrentTime()
            dates.add(current)
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

    private fun getDiningTabByType(type: DiningType): Int {
        return when (type) {
            DiningType.Breakfast -> 0
            DiningType.Lunch -> 1
            DiningType.Dinner -> 2
            DiningType.NextBreakfast -> 0
        }
    }

    private fun initOnRefreshDiningList() {
        binding.swipeRefreshLayoutDining.setOnRefreshListener {
            viewModel.getDining(viewModel.selectedDate.value)
            binding.swipeRefreshLayoutDining.isRefreshing = false
        }
    }

    override fun onNewIntent(intent: Intent?) {
        intent.apply {
            this?.action = Intent.ACTION_VIEW
            setIntent(this)
        }
        super.onNewIntent(intent)
        onActionView()
        selectInitialPositions()
    }
}