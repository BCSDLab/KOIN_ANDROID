package `in`.koreatech.koin.ui.main.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
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
import `in`.koreatech.koin.core.abtest.ABTestConstants.BENEFIT_STORE
import `in`.koreatech.koin.ui.store.activity.CallBenefitStoreActivity
import `in`.koreatech.koin.core.activity.WebViewActivity
import `in`.koreatech.koin.core.analytics.EventAction
import `in`.koreatech.koin.core.analytics.EventExtra
import `in`.koreatech.koin.core.analytics.EventLogger
import `in`.koreatech.koin.core.constant.AnalyticsConstant
import `in`.koreatech.koin.core.util.dataBinding
import `in`.koreatech.koin.core.viewpager.HorizontalMarginItemDecoration
import `in`.koreatech.koin.core.viewpager.enableAutoScroll
import `in`.koreatech.koin.data.constant.URLConstant
import `in`.koreatech.koin.data.util.localized
import `in`.koreatech.koin.data.util.todayOrTomorrow
import `in`.koreatech.koin.databinding.ActivityMainBinding
import `in`.koreatech.koin.domain.model.bus.BusType
import `in`.koreatech.koin.domain.model.bus.timer.BusArrivalInfo
import `in`.koreatech.koin.domain.model.dining.DiningPlace
import `in`.koreatech.koin.ui.article.ArticleActivity
import `in`.koreatech.koin.ui.bus.BusActivity
import `in`.koreatech.koin.ui.forceupdate.ForceUpdateActivity
import `in`.koreatech.koin.ui.main.adapter.BusPagerAdapter
import `in`.koreatech.koin.ui.main.adapter.DiningContainerViewPager2Adapter
import `in`.koreatech.koin.ui.main.adapter.HotArticleAdapter
import `in`.koreatech.koin.ui.main.adapter.StoreCategoriesRecyclerAdapter
import `in`.koreatech.koin.ui.main.viewmodel.MainActivityViewModel
import `in`.koreatech.koin.ui.navigation.KoinNavigationDrawerTimeActivity
import `in`.koreatech.koin.ui.navigation.state.MenuState
import `in`.koreatech.koin.ui.store.contract.StoreActivityContract
import `in`.koreatech.koin.util.ext.observeLiveData
import kotlinx.coroutines.launch
import kotlin.properties.Delegates

@AndroidEntryPoint
class MainActivity : KoinNavigationDrawerTimeActivity() {
    override val menuState = MenuState.Main
    private val binding by dataBinding<ActivityMainBinding>(R.layout.activity_main)
    override val screenTitle = "코인 - 메인"
    private val viewModel by viewModels<MainActivityViewModel>()
    private lateinit var diningTooltip: Balloon

    private val hotArticleAdapter = HotArticleAdapter(
        onClick = {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("koin://article/activity?fragment=article_detail&article_id=${it.id}&board_id=${it.board.id}")
            }
            startActivity(intent)
        }
    )

    private val busPagerAdapter = BusPagerAdapter().apply {
        setOnCardClickListener {
            callDrawerItem(R.id.navi_item_bus, Bundle())
            EventLogger.logClickEvent(
                EventAction.CAMPUS,
                AnalyticsConstant.Label.MAIN_BUS,
                getString(R.string.bus)
            )
        }
        setOnSwitchClickListener {
            viewModel.switchBusNode()
            EventLogger.logClickEvent(
                EventAction.CAMPUS,
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
                EventAction.BUSINESS,
                AnalyticsConstant.Label.MAIN_SHOP_CATEGORIES,
                name,
                EventExtra(AnalyticsConstant.PREVIOUS_PAGE, "메인"),
                EventExtra(AnalyticsConstant.CURRENT_PAGE, name),
                EventExtra(AnalyticsConstant.DURATION_TIME, getElapsedTimeAndReset().toString())
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
        viewModel.postABTestAssign(BENEFIT_STORE.experimentTitle)
        storeListButton.setOnClickListener {
            gotoStoreActivity(0)
        }
        callBenefitStoreListButton.setOnClickListener{
            EventLogger.logClickEvent(
                EventAction.BUSINESS,
                AnalyticsConstant.Label.MAIN_SHOP_BENEFIT,
                "전화주문혜택",
                EventExtra(AnalyticsConstant.PREVIOUS_PAGE, "메인"),
                EventExtra(AnalyticsConstant.CURRENT_PAGE, "benefit"),
                EventExtra(AnalyticsConstant.DURATION_TIME, getElapsedTimeAndReset().toString())
            )
            val intent = Intent(this@MainActivity, CallBenefitStoreActivity::class.java)
            startActivity(intent)
        }
        buttonCategory.setOnClickListener {
            toggleNavigationDrawer()
        }

        viewPagerHotArticle.apply {
            adapter = hotArticleAdapter
            offscreenPageLimit = 3
            enableAutoScroll(this@MainActivity, 5_000)
        }
        TabLayoutMediator(tabHotArticle, viewPagerHotArticle) { _, _ -> }.attach()

        textSeeMoreArticle.setOnClickListener {
            startActivity(Intent(this@MainActivity, ArticleActivity::class.java))
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
            adapter = storeCategoriesRecyclerAdapter
        }

        mainSwipeRefreshLayout.setOnRefreshListener {
            viewModel.updateDining()
        }

//        diningContainer.setOnClickListener {
//            callDrawerItem(R.id.navi_item_dining)
//        }

        pagerDiningContainer.adapter = diningContainerAdapter
        pagerDiningContainer.offscreenPageLimit = 3

        TabLayoutMediator(tabDining, pagerDiningContainer) { tab, position ->
            tab.text = DiningPlace.entries[position].place
        }.attach()

        tabDining.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewModel.setSelectedPosition(tab.position)
                EventLogger.logClickEvent(
                    EventAction.CAMPUS,
                    AnalyticsConstant.Label.MAIN_MENU_CORNER,
                    tab.text.toString()
                )
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}


            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }

    private fun initViewModel() = with(viewModel) {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.hotArticles.collect {
                    hotArticleAdapter.submitList(it)
                }
            }
        }
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
        binding.recyclerViewStoreCategory.visibility= View.GONE
        binding.storeButtonLayout.visibility= View.VISIBLE
        observeLiveData(variableName){
            when(viewModel.variableName.value){
                "A" -> {
                    EventLogger.logCustomEvent(
                        action = "A/B_TEST",
                        category = "a/b test 로깅(3차 스프린트, 혜택페이지)",
                        label = "BUSINESS_benefit_1",
                        value = "혜택X"
                    )
                    binding.storeButtonLayout.visibility= View.GONE
                    binding.recyclerViewStoreCategory.visibility= View.VISIBLE
                }
                "B" -> {
                    EventLogger.logCustomEvent(
                        action = "A/B_TEST",
                        category = "a/b test 로깅(3차 스프린트, 혜택페이지)",
                        label = "BUSINESS_benefit_1",
                        value = "혜택O"
                    )
                    binding.storeButtonLayout.visibility= View.VISIBLE
                    binding.recyclerViewStoreCategory.visibility= View.GONE
                }
                else -> {
                    EventLogger.logCustomEvent(
                        action = "A/B_TEST",
                        category = "a/b test 로깅(3차 스프린트, 혜택페이지)",
                        label = "BUSINESS_benefit_1",
                        value = "혜택X"
                    )
                    binding.storeButtonLayout.visibility= View.GONE
                    binding.recyclerViewStoreCategory.visibility= View.VISIBLE
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
                    EventAction.CAMPUS,
                    AnalyticsConstant.Label.MAIN_BUS_SCROLL,
                    busArrivalInfos[prev % 3].localized(this@MainActivity) + ">" + busArrivalInfos[position % 3].localized(
                        this@MainActivity
                    )
                )
                prev = position
            }
        }.also { binding.busViewPager.registerOnPageChangeCallback(it) }
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
