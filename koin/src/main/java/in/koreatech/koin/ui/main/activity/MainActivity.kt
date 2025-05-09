package `in`.koreatech.koin.ui.main.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import `in`.koreatech.bus.BusSearchActivity
import `in`.koreatech.bus.BusTimetableActivity
import `in`.koreatech.bus.screen.MainEntryView
import `in`.koreatech.koin.R
import `in`.koreatech.koin.core.abtest.Experiment
import `in`.koreatech.koin.core.abtest.ExperimentGroup
import `in`.koreatech.koin.core.activity.WebViewActivity
import `in`.koreatech.koin.core.analytics.AnalyticsConstant
import `in`.koreatech.koin.core.analytics.EventAction
import `in`.koreatech.koin.core.analytics.EventExtra
import `in`.koreatech.koin.core.analytics.EventLogger
import `in`.koreatech.koin.core.analytics.EventUtils
import `in`.koreatech.koin.core.designsystem.theme.KoinTheme
import `in`.koreatech.koin.core.navigation.Navigator
import `in`.koreatech.koin.core.navigation.SchemeType
import `in`.koreatech.koin.core.navigation.utils.EXTRA_ARTICLE_ID
import `in`.koreatech.koin.core.navigation.utils.EXTRA_BOARD_ID
import `in`.koreatech.koin.core.navigation.utils.EXTRA_CHAT_ROOM_ID
import `in`.koreatech.koin.core.navigation.utils.EXTRA_ID
import `in`.koreatech.koin.core.navigation.utils.EXTRA_TYPE
import `in`.koreatech.koin.core.util.dataBinding
import `in`.koreatech.koin.core.viewpager.enableAutoScroll
import `in`.koreatech.koin.databinding.ActivityMainBinding
import `in`.koreatech.koin.domain.model.article.ArticleNotiType
import `in`.koreatech.koin.domain.model.store.StoreCategories
import `in`.koreatech.koin.feature.banner.ui.BannerActivity
import `in`.koreatech.koin.ui.article.ArticleActivity
import `in`.koreatech.koin.ui.main.adapter.ArticleMainAdapter
import `in`.koreatech.koin.ui.main.adapter.StoreCategoriesRecyclerAdapter
import `in`.koreatech.koin.ui.main.viewmodel.MainActivityViewModel
import `in`.koreatech.koin.ui.main.widget.DiningWidget
import `in`.koreatech.koin.ui.navigation.KoinNavigationDrawerTimeActivity
import `in`.koreatech.koin.ui.navigation.state.MenuState
import `in`.koreatech.koin.ui.store.activity.CallBenefitStoreActivity
import `in`.koreatech.koin.ui.store.contract.StoreActivityContract
import `in`.koreatech.koin.util.ext.observeLiveData
import javax.inject.Inject
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : KoinNavigationDrawerTimeActivity() {
    override val menuState = MenuState.Main
    private val binding by dataBinding<ActivityMainBinding>(R.layout.activity_main)
    override val screenTitle = "코인 - 메인"
    private val viewModel by viewModels<MainActivityViewModel>()

    private var scrollPercentage = 0.0f

    @Inject
    lateinit var navigator: Navigator

    private val articleMainAdapter =
        ArticleMainAdapter(
            onNotiClick = {
                EventLogger.logClickEvent(
                    EventAction.CAMPUS,
                    AnalyticsConstant.Label.TO_MANAGE_KEYWORD,
                    it.value
                )
                val intent =
                    Intent(Intent.ACTION_VIEW).apply {
                        data =
                            when (it.type) {
                                ArticleNotiType.KEYWORD -> Uri.parse("koin://article/activity?fragment=article_keyword")
                                ArticleNotiType.LOST_AND_FOUND -> Uri.parse("koin://article/activity?fragment=article_lost_and_found")
                            }
                    }
                startActivity(intent)
            },
            onArticleClick = {
                EventLogger.logClickEvent(
                    EventAction.CAMPUS,
                    AnalyticsConstant.Label.POPULAR_NOTICE_BANNER,
                    it.title
                )
                val intent =
                    Intent(Intent.ACTION_VIEW).apply {
                        data =
                            Uri.parse("koin://article/activity?fragment=article_detail&article_id=${it.id}&board_id=${it.boardId}")
                    }
                startActivity(intent)
            }
        )

    private val storeCategoriesRecyclerAdapter =
        StoreCategoriesRecyclerAdapter().apply {
            setOnItemClickListener { id, name ->
                if (id == 0) {
                    startActivity(Intent(this@MainActivity, CallBenefitStoreActivity::class.java))
                    EventLogger.logClickEvent(
                        EventAction.BUSINESS,
                        AnalyticsConstant.Label.MAIN_SHOP_BENEFIT,
                        name,
                        EventExtra(AnalyticsConstant.PREVIOUS_PAGE, "메인"),
                        EventExtra(AnalyticsConstant.CURRENT_PAGE, "benefit"),
                        EventExtra(
                            AnalyticsConstant.DURATION_TIME,
                            getElapsedTimeAndReset().toString()
                        )
                    )
                } else {
                    EventLogger.logClickEvent(
                        EventAction.BUSINESS,
                        AnalyticsConstant.Label.MAIN_SHOP_CATEGORIES,
                        name,
                        EventExtra(AnalyticsConstant.PREVIOUS_PAGE, "메인"),
                        EventExtra(AnalyticsConstant.CURRENT_PAGE, name),
                        EventExtra(
                            AnalyticsConstant.DURATION_TIME,
                            getElapsedTimeAndReset().toString()
                        )
                    )
                    gotoStoreActivity(id)
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initView()
        initViewModel()
        initBanner()
        handleIntent()
    }

    override fun onResume() {
        super.onResume()
        viewModel.checkKeywordNotiContent()
        viewModel.updateDining()
    }

    private fun initView() = with(binding) {
        viewModel.checkKeywordNotiContent()
        initArticleBannerABTest()
        binding.nestedScrollViewMain.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            val offset = binding.nestedScrollViewMain.computeVerticalScrollOffset()
            val extent = binding.nestedScrollViewMain.computeVerticalScrollExtent()
            val range = binding.nestedScrollViewMain.computeVerticalScrollRange()

            val newScrollPercentage = 100.0f * offset / (range - extent)
            if (EventUtils.didCrossedScrollThreshold(
                    scrollPercentage,
                    newScrollPercentage
                ) && scrollPercentage.toDouble() != .0
            ) {
                EventLogger.logScrollEvent(
                    EventAction.CAMPUS,
                    AnalyticsConstant.Label.MAIN_SCROLL,
                    "70%"
                )
            }
            scrollPercentage = 100.0f * offset / (range - extent)
        }
        viewModel.postABTestAssign(Experiment.BENEFIT_STORE.experimentTitle)

        storeListButton.setOnClickListener {
            gotoStoreActivity(0)
        }
        callBenefitStoreListButton.setOnClickListener {
            EventLogger.logClickEvent(
                EventAction.BUSINESS,
                AnalyticsConstant.Label.MAIN_SHOP_BENEFIT,
                "전화주문혜택",
                EventExtra(AnalyticsConstant.PREVIOUS_PAGE, "메인"),
                EventExtra(AnalyticsConstant.CURRENT_PAGE, "benefit"),
                EventExtra(
                    AnalyticsConstant.DURATION_TIME,
                    getElapsedTimeAndReset().toString()
                )
            )
            val intent = Intent(this@MainActivity, CallBenefitStoreActivity::class.java)
            startActivity(intent)
        }
        buttonCategory.setOnClickListener {
            toggleNavigationDrawer()
        }

        viewPagerHotArticle.apply {
            adapter = articleMainAdapter
            offscreenPageLimit = 3
            enableAutoScroll(this@MainActivity, 5_000)
        }
        TabLayoutMediator(tabHotArticle, viewPagerHotArticle) { _, _ -> }.attach()

        textSeeMoreArticle.setOnClickListener {
            EventLogger.logClickEvent(
                EventAction.CAMPUS,
                AnalyticsConstant.Label.APP_MAIN_NOTICE_DETAIL,
                getString(R.string.article_more)
            )
            startActivity(Intent(this@MainActivity, ArticleActivity::class.java))
        }

        busComposeView.apply {
            setContent {
                MainEntryView(
                    onShuttleTicketClicked = {
                        EventLogger.logCampusClickEvent(
                            "shuttle_ticket",
                            "셔틀 탑승권"
                        )
                        val intent = Intent(this@MainActivity, WebViewActivity::class.java)
                        intent.putExtra("url", "https://koreatech.unibus.kr/")
                        startActivity(intent)
                    },
                    onTimetableCardClicked = {
                        EventLogger.logCampusClickEvent(
                            "main_bus_timetable",
                            "버스 시간표 바로가기"
                        )
                        val intent = Intent(this@MainActivity, BusTimetableActivity::class.java)
                        startActivity(intent)
                    },
                    onSearchCardClicked = {
                        EventLogger.logCampusClickEvent(
                            "main_bus_search",
                            "가장 빠른 버스 조회하기"
                        )
                        val intent = Intent(this@MainActivity, BusSearchActivity::class.java)
                        startActivity(intent)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                )
            }
        }

        recyclerViewStoreCategory.apply {
            layoutManager = GridLayoutManager(this@MainActivity, 6)
            adapter = storeCategoriesRecyclerAdapter
        }

        mainSwipeRefreshLayout.setOnRefreshListener {
            viewModel.updateDining()
        }

        diningComposeView.apply {
            setContent {
                KoinTheme {
                    val diningData by viewModel.diningData.collectAsState()
                    val selectedPosition by viewModel.selectedPosition.collectAsState()
                    val selectedType by viewModel.selectedType.collectAsState()
                    val diningABTestExperimentGroup by viewModel.diningABTestExperimentGroup.collectAsState()

                    DiningWidget(
                        diningData = diningData,
                        selectedPosition = selectedPosition,
                        selectedType = selectedType,
                        diningABTestExperimentGroup = diningABTestExperimentGroup
                    )
                }
            }
        }
    }

    private fun initViewModel() = with(viewModel) {
        getStoreCategories(StoreCategories(-1, R.drawable.ic_benefit_icon, "혜택"))

        observeLiveData(variableName) {
            when (viewModel.variableName.value) {
                ExperimentGroup.A -> {
                    binding.storeButtonLayout.visibility = View.GONE
                    binding.recyclerViewStoreCategory.visibility = View.VISIBLE
                }

                ExperimentGroup.B -> {
                    binding.storeButtonLayout.visibility = View.VISIBLE
                    binding.recyclerViewStoreCategory.visibility = View.GONE
                }

                else -> {
                    binding.storeButtonLayout.visibility = View.GONE
                    binding.recyclerViewStoreCategory.visibility = View.VISIBLE
                }
            }
        }
        observeLiveData(isLoading) {
            binding.mainSwipeRefreshLayout.isRefreshing = it
        }

        observeLiveData(storeCategories) {
            storeCategoriesRecyclerAdapter.submitList(it)
        }
        binding.recyclerViewStoreCategory.visibility = View.GONE
        binding.storeButtonLayout.visibility = View.VISIBLE
    }

    private fun initBanner() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.isBannerRefusal.collectLatest {
                    if (it == false) {
                        val intent = Intent(this@MainActivity, BannerActivity::class.java)
                        startActivity(intent)
                    }
                }
            }
        }
    }

    private fun gotoStoreActivity(id: Int) {
        val bundle = Bundle()
        bundle.putInt(StoreActivityContract.STORE_CATEGORY, id)
        callDrawerItem(R.id.navi_item_store, bundle)
    }

    private fun handleIntent() {
        val targetId = intent.getIntExtra(EXTRA_ID, -1)
        val targetBoardId = intent.getIntExtra(EXTRA_BOARD_ID, -1)
        val targetArticleId = intent.getIntExtra(EXTRA_ARTICLE_ID, -1)
        val targetChatId = intent.getIntExtra(EXTRA_CHAT_ROOM_ID, -1)
        val type = intent.getStringExtra(EXTRA_TYPE) ?: ""

        when (type) {
            SchemeType.SHOP.type -> {
                val intent =
                    navigator.navigateToShop(
                        context = this,
                        targetId = Pair(EXTRA_ID, targetId),
                        type = Pair(EXTRA_TYPE, type)
                    )
                startActivity(intent)
            }

            SchemeType.DINING.type -> {
                val intent =
                    navigator.navigateToDinging(
                        context = this,
                        targetId = Pair(EXTRA_ID, targetId),
                        type = Pair(EXTRA_TYPE, type)
                    )
                startActivity(intent)
            }

            SchemeType.ARTICLE.type -> {
                val intent =
                    navigator.navigateToArticle(
                        context = this,
                        targetId = Pair(EXTRA_ID, targetId),
                        targetBoardId = Pair(EXTRA_BOARD_ID, targetBoardId),
                        type = Pair(EXTRA_TYPE, type)
                    )
                startActivity(intent)
            }

            SchemeType.CHAT.type -> {
                val intent =
                    navigator.navigateToChat(
                        context = this,
                        targetArticleId = Pair(EXTRA_ARTICLE_ID, targetArticleId),
                        targetChatId = Pair(EXTRA_CHAT_ROOM_ID, targetChatId),
                        type = Pair(EXTRA_TYPE, type)
                    )
                startActivity(intent)
            }

            else -> {
                // Do nothing
            }
        }
    }

    private fun initArticleBannerABTest() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.articleMain.collectLatest {
                    articleMainAdapter.submitList(it)
                }
            }
        }
    }
}
