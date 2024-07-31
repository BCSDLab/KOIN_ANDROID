package `in`.koreatech.koin.ui.main.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.skydoves.balloon.ArrowOrientation
import com.skydoves.balloon.ArrowOrientationRules
import com.skydoves.balloon.ArrowPositionRules
import com.skydoves.balloon.Balloon
import com.skydoves.balloon.BalloonAnimation
import com.skydoves.balloon.BalloonSizeSpec
import dagger.hilt.android.AndroidEntryPoint
import `in`.koreatech.koin.R
import `in`.koreatech.koin.core.activity.WebViewActivity
import `in`.koreatech.koin.core.analytics.EventLogger
import `in`.koreatech.koin.core.constant.AnalyticsConstant
import `in`.koreatech.koin.core.util.dataBinding
import `in`.koreatech.koin.core.viewpager.HorizontalMarginItemDecoration
import `in`.koreatech.koin.data.constant.URLConstant
import `in`.koreatech.koin.data.util.localized
import `in`.koreatech.koin.data.util.todayOrTomorrow
import `in`.koreatech.koin.databinding.ActivityMainBinding
import `in`.koreatech.koin.domain.model.bus.BusType
import `in`.koreatech.koin.domain.model.bus.timer.BusArrivalInfo
import `in`.koreatech.koin.domain.model.dining.DiningPlace
import `in`.koreatech.koin.ui.bus.BusActivity
import `in`.koreatech.koin.ui.main.adapter.BusPagerAdapter
import `in`.koreatech.koin.ui.main.adapter.DiningContainerViewPager2Adapter
import `in`.koreatech.koin.ui.main.adapter.StoreCategoriesRecyclerAdapter
import `in`.koreatech.koin.ui.main.viewmodel.MainActivityViewModel
import `in`.koreatech.koin.ui.navigation.KoinNavigationDrawerActivity
import `in`.koreatech.koin.ui.navigation.state.MenuState
import `in`.koreatech.koin.ui.store.contract.StoreActivityContract
import `in`.koreatech.koin.util.ext.observeLiveData
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : KoinNavigationDrawerActivity() {
    override val menuState = MenuState.Main
    private val binding by dataBinding<ActivityMainBinding>(R.layout.activity_main)
    override val screenTitle = "코인 - 메인"
    private val viewModel by viewModels<MainActivityViewModel>()
    private lateinit var diningTooltip: Balloon

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
        setOnGotoClickListener { type ->
            when (type) {
                BusType.Shuttle -> {
                    Intent(this@MainActivity, WebViewActivity::class.java).apply {
                        putExtra("url", URLConstant.UNIBUS)
                    }.run(::startActivity)
                }

                BusType.Express -> {
                    Intent(this@MainActivity, BusActivity::class.java).apply {
                        putExtra("tab", 2)
                        putExtra("timetableMenu", type.busTypeString)
                    }.run(::startActivity)
                }

                BusType.City -> {
                    Intent(this@MainActivity, BusActivity::class.java).apply {
                        putExtra("tab", 2)
                        putExtra("timetableMenu", type.busTypeString)
                    }.run(::startActivity)
                }

                else -> {}
            }
        }
    }
    private lateinit var busViewPagerScrollCallback: ViewPager2.OnPageChangeCallback

    private val diningContainerAdapter by lazy { DiningContainerViewPager2Adapter(this) }

    private val storeCategoriesRecyclerAdapter = StoreCategoriesRecyclerAdapter().apply {
        setOnItemClickListener { id, name ->
            EventLogger.logClickEvent(
                AnalyticsConstant.Domain.BUSINESS,
                AnalyticsConstant.Label.MAIN_SHOP_CATEGORIES,
                name
            )
            gotoStoreActivity(id)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initView()
        initDiningTooltip()
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

            val nextItemPx = resources.getDimension(R.dimen.view_pager_next_item_visible_dp)
            val currentItemMarginPx = resources.getDimension(R.dimen.view_pager_item_margin)

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

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                showDiningTooltip.collect {
                    if (it) {
                        diningTooltip.showAlignRight(binding.textViewDiningTitle)
                        viewModel.updateShouldShowDiningTooltip(false)
                    }
                }
            }
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

    private fun initDiningTooltip() {
        diningTooltip = Balloon.Builder(this)
            .setHeight(BalloonSizeSpec.WRAP)
            .setWidth(BalloonSizeSpec.WRAP)
            .setText(getString(R.string.dining_image_tooltip))
            .setTextColorResource(R.color.black)
            .setBackgroundColorResource(R.color.gray3)
            .setTextSize(12f)
            .setArrowOrientationRules(ArrowOrientationRules.ALIGN_FIXED)
            .setArrowOrientation(ArrowOrientation.BOTTOM)
            .setArrowPositionRules(ArrowPositionRules.ALIGN_BALLOON)
            .setArrowSize(10)
            .setArrowPosition(0.85f)
            .setPaddingVertical(4)
            .setPaddingHorizontal(5)
            .setMarginLeft(10)
            .setCornerRadius(8f)
            .setBalloonAnimation(BalloonAnimation.FADE)
            .build()
    }
}
