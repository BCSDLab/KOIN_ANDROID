package `in`.koreatech.koin.ui.main.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import `in`.koreatech.koin.R
import `in`.koreatech.koin.core.util.dataBinding
import `in`.koreatech.koin.core.viewpager.HorizontalMarginItemDecoration
import `in`.koreatech.koin.core.analytics.EventLogger
import `in`.koreatech.koin.core.constant.AnalyticsConstant
import `in`.koreatech.koin.data.util.localized
import `in`.koreatech.koin.data.util.todayOrTomorrow
import `in`.koreatech.koin.databinding.ActivityMainBinding
import `in`.koreatech.koin.domain.model.bus.timer.BusArrivalInfo
import `in`.koreatech.koin.domain.model.dining.DiningPlace
import `in`.koreatech.koin.ui.main.adapter.BusPagerAdapter
import `in`.koreatech.koin.ui.main.adapter.DiningContainerViewPager2Adapter
import `in`.koreatech.koin.ui.main.adapter.StoreCategoriesRecyclerAdapter
import `in`.koreatech.koin.ui.main.viewmodel.MainActivityViewModel
import `in`.koreatech.koin.ui.navigation.KoinNavigationDrawerActivity
import `in`.koreatech.koin.ui.navigation.state.MenuState
import `in`.koreatech.koin.ui.store.contract.StoreActivityContract
import `in`.koreatech.koin.util.ext.observeLiveData
import androidx.activity.result.contract.ActivityResultContracts

@AndroidEntryPoint
class MainActivity : KoinNavigationDrawerActivity() {
    override val menuState = MenuState.Main
    private val binding by dataBinding<ActivityMainBinding>(R.layout.activity_main)
    override val screenTitle = "코인 - 메인"
    private val viewModel by viewModels<MainActivityViewModel>()

    private val busPagerAdapter = BusPagerAdapter().apply {
        setOnCardClickListener {
            callDrawerItem(R.id.navi_item_bus, Bundle())
            EventLogger.logClickEvent(
                AnalyticsConstant.Domain.CAMPUS,
                AnalyticsConstant.Label.MAIN_BUS,
                getString(R.string.bus)
            )
        }
        setOnSwitchClickListener {
            viewModel.switchBusNode()
            EventLogger.logClickEvent(
                AnalyticsConstant.Domain.CAMPUS,
                AnalyticsConstant.Label.MAIN_BUS_CHANGETOFROM,
                it.localized(this@MainActivity)
            )
        }
    }
    private lateinit var busViewPagerScrollCallback: ViewPager2.OnPageChangeCallback

    private val diningContainerAdapter by lazy { DiningContainerViewPager2Adapter(this) }

    private val storeCategoriesRecyclerAdapter = StoreCategoriesRecyclerAdapter().apply {
        setOnItemClickListener {
            gotoStoreActivity(it)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initView()
        initViewModel()
    }

    override fun onResume() {
        super.onResume()
        viewModel.updateDining()
    }

    private fun initView() = with(binding) {
        buttonCategory.setOnClickListener {
            toggleNavigationDrawer()
            EventLogger.logClickEvent(
                AnalyticsConstant.Domain.USER,
                AnalyticsConstant.Label.HAMBURGER,
                getString(R.string.hamburger)
            )
        }

        busViewPager.apply {
            adapter = busPagerAdapter
            offscreenPageLimit = 3
            currentItem = Int.MAX_VALUE / 2

            // val nextItemPx = resources.getDimension(R.dimen.view_pager_next_item_visible_dp)
            // val currentItemMarginPx = resources.getDimension(R.dimen.view_pager_item_margin)
            // setPageTransformer(ScaledViewPager2Transformation(currentItemMarginPx, nextItemPx))
            addItemDecoration(
                HorizontalMarginItemDecoration(
                    this@MainActivity,
                    R.dimen.view_pager_item_margin
                )
            )
        }

        recyclerViewStoreCategory.apply {
            layoutManager =
                LinearLayoutManager(this@MainActivity, RecyclerView.HORIZONTAL, false)
            //adapter = storeCategoryRecyclerAdapter
            adapter = storeCategoriesRecyclerAdapter
        }

        mainSwipeRefreshLayout.setOnRefreshListener {
            viewModel.updateDining()
        }

//        diningContainer.setOnClickListener {
//            callDrawerItem(R.id.navi_item_dining)
//        }

        pagerDiningContainer.adapter = diningContainerAdapter

        TabLayoutMediator(tabDining, pagerDiningContainer) { tab, position ->
            tab.text = DiningPlace.entries[position].place
        }.attach()

        tabDining.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewModel.setSelectedPosition(tab.position)
                EventLogger.logClickEvent(
                    AnalyticsConstant.Domain.CAMPUS,
                    AnalyticsConstant.Label.MAIN_MENU_CORNER,
                    tab.text.toString()
                )
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}

            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }

    private fun initViewModel() = with(viewModel) {
        observeLiveData(isLoading) {
            binding.mainSwipeRefreshLayout.isRefreshing = it
        }

        observeLiveData(selectedType) {
            binding.textViewDiningTodayOrTomorrow.text = it.todayOrTomorrow(this@MainActivity)
        }


        observeLiveData(busTimer) {
            busPagerAdapter.setBusTimerItems(it)
            if (this@MainActivity::busViewPagerScrollCallback.isInitialized.not()) {
                initBusViewPagerScrollCallback(it)
            }
        }
        observeLiveData(storeCategories) {
            storeCategoriesRecyclerAdapter.submitList(it.drop(1))
        }
    }

    private fun initBusViewPagerScrollCallback(busArrivalInfos: List<BusArrivalInfo>) {
        busViewPagerScrollCallback = object : ViewPager2.OnPageChangeCallback() {
            var prev = 0
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                EventLogger.logScrollEvent(
                    AnalyticsConstant.Domain.CAMPUS,
                    AnalyticsConstant.Label.MAIN_BUS_SCROLL,
                    busArrivalInfos[prev % 3].localized(this@MainActivity) + ">" + busArrivalInfos[position % 3].localized(
                        this@MainActivity
                    )
                )
                prev = position
            }
        }.also { binding.busViewPager.registerOnPageChangeCallback(it) }
    }

    private fun gotoStoreActivity(position: Int) {
        val bundle = Bundle()
        bundle.putInt(StoreActivityContract.STORE_CATEGORY, position)
        callDrawerItem(R.id.navi_item_store, bundle)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (this@MainActivity::busViewPagerScrollCallback.isInitialized)
            binding.busViewPager.unregisterOnPageChangeCallback(busViewPagerScrollCallback)
    }
}
