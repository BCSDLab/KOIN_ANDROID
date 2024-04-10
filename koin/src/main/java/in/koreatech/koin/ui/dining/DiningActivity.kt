package `in`.koreatech.koin.ui.dining

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import `in`.koreatech.koin.R
import `in`.koreatech.koin.core.appbar.AppBarBase
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initCalendar()
        initViewPager()

        withLoading(this, viewModel)
        viewModel.getDining()

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.selectedDate.collect {
                    viewModel.getDining(it)
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

    private fun initViewPager() {
        with(binding) {
            diningViewPager.apply {
                offscreenPageLimit = 3
                adapter = DiningItemsViewPager2Adapter(this@DiningActivity)
            }
            TabLayoutMediator(tabsDiningTime, diningViewPager) { tab, position ->
                tab.text = when (position) {
                    0 -> getString(R.string.dining_breakfast)
                    1 -> getString(R.string.dining_lunch)
                    2 -> getString(R.string.dining_dinner)
                    else -> throw IllegalArgumentException("Position must be lower than ${diningViewPager.offscreenPageLimit}")
                }
            }.attach()
            when(DiningUtil.getCurrentType()) {
                DiningType.Breakfast -> tabsDiningTime.selectTab(tabsDiningTime.getTabAt(0))
                DiningType.Lunch -> tabsDiningTime.selectTab(tabsDiningTime.getTabAt(1))
                DiningType.Dinner -> tabsDiningTime.selectTab(tabsDiningTime.getTabAt(2))
            }
        }
    }

    private fun initCalendar() {
        with(binding) {
            recyclerViewCalendar.adapter = diningDateAdapter
            val current = TimeUtil.getCurrentTime()
            dates.add(current)
            repeat(7) {
                dates.add(0, TimeUtil.getPreviousDayDate(dates.first()))
            }
            repeat(7) {
                dates.add(TimeUtil.getNextDayDate(dates.last()))
            }
            diningDateAdapter.submitList(dates)

            val todayPos = dates.size / 2
            diningDateAdapter.setSelectedPosition(todayPos)
            scrollDateTodayToCenter(todayPos)
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
}