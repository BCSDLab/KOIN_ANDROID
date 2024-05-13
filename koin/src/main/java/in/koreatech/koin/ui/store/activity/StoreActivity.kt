package `in`.koreatech.koin.ui.store.activity

import android.graphics.Rect
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MotionEvent
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import dagger.hilt.android.AndroidEntryPoint
import `in`.koreatech.koin.R
import `in`.koreatech.koin.core.analytics.EventLogger
import `in`.koreatech.koin.core.appbar.AppBarBase
import `in`.koreatech.koin.core.constant.AnalyticsConstant
import `in`.koreatech.koin.core.util.dataBinding
import `in`.koreatech.koin.core.viewpager.HorizontalMarginItemDecoration
import `in`.koreatech.koin.databinding.StoreActivityMainBinding
import `in`.koreatech.koin.domain.model.store.StoreCategory
import `in`.koreatech.koin.domain.model.store.toStoreCategory
import `in`.koreatech.koin.ui.navigation.KoinNavigationDrawerActivity
import `in`.koreatech.koin.ui.navigation.state.MenuState
import `in`.koreatech.koin.ui.store.adapter.StoreCategoriesRecyclerAdapter
import `in`.koreatech.koin.ui.store.adapter.StoreEventPagerAdapter
import `in`.koreatech.koin.ui.store.adapter.StoreRecyclerAdapter
import `in`.koreatech.koin.ui.store.contract.StoreActivityContract
import `in`.koreatech.koin.ui.store.contract.StoreDetailActivityContract
import `in`.koreatech.koin.ui.store.viewmodel.StoreViewModel
import `in`.koreatech.koin.util.ext.dpToPx
import `in`.koreatech.koin.util.ext.hideSoftKeyboard
import `in`.koreatech.koin.util.ext.observeLiveData
import `in`.koreatech.koin.util.ext.showSoftKeyboard
import kotlinx.coroutines.launch

@AndroidEntryPoint
class StoreActivity : KoinNavigationDrawerActivity() {
    override val menuState = MenuState.Store

    private val binding by dataBinding<StoreActivityMainBinding>(R.layout.store_activity_main)
    override val screenTitle = "상점"
    private val viewModel by viewModels<StoreViewModel>()

    private val storeDetailContract = registerForActivityResult(StoreDetailActivityContract()) {

    }

    private val viewPagerHandler = Handler(Looper.getMainLooper())
    private val viewPagerDelayTime = 10000L

    private val storeAdapter = StoreRecyclerAdapter().apply {
        setOnItemClickListener {
            storeDetailContract.launch(it.uid)
            EventLogger.logClickEvent(
                AnalyticsConstant.Domain.BUSINESS,
                AnalyticsConstant.Label.SHOP_CLICK,
                it.name
            )
        }
    }

    private val storeEventPagerAdapter = StoreEventPagerAdapter().apply {
        setOnItemClickListener {
            EventLogger.logClickEvent(
                AnalyticsConstant.Domain.BUSINESS,
                AnalyticsConstant.Label.SHOP_CATEGORIES_EVENT,
                it.shopName
            )
            storeDetailContract.launch(it.shopId)
        }
    }

    private val storeCategoriesAdapter = StoreCategoriesRecyclerAdapter().apply {
        setOnItemClickListener {
            viewModel.setCategory(it.toStoreCategory())
            binding.searchEditText.text.clear()
            val eventValue = getStoreCategoryName(viewModel.category.value)
            EventLogger.logClickEvent(
                AnalyticsConstant.Domain.BUSINESS,
                AnalyticsConstant.Label.SHOP_CATEGORIES,
                eventValue
            )
        }
    }

    private var isSearchMode: Boolean = false
        set(value) {
            if (value) showSoftKeyboard()
            else hideSoftKeyboard()
            binding.categoryConstraintLayout.isVisible = !value
            field = value
        }

    private var showRemoveQueryButton: Boolean = false
        set(value) {
            if (!value) {
                binding.searchImageView.background = ContextCompat.getDrawable(
                    this,
                    R.drawable.ic_search
                )
                binding.searchImageView.layoutParams.apply {
                    width = dpToPx(24)
                    height = dpToPx(24)
                }
            } else {
                binding.searchImageView.background = ContextCompat.getDrawable(
                    this,
                    R.drawable.ic_search_close
                )
                binding.searchImageView.layoutParams.apply {
                    width = dpToPx(16)
                    height = dpToPx(16)
                }
            }
            field = value
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initViewModel()
        initView()

        val initStoreCategory =
            intent.extras?.getInt(StoreActivityContract.STORE_CATEGORY)?.toStoreCategory()

        storeCategoriesAdapter.selectPosition =
            intent.extras?.getInt(StoreActivityContract.STORE_CATEGORY)?.minus(2)
        viewModel.setCategory(initStoreCategory)
    }

    override fun onBackPressed() {
        if (binding.searchEditText.hasFocus()) {
            binding.searchEditText.clearFocus()
            return
        }
        super.onBackPressed()
    }

    private fun initView() {
        binding.koinBaseAppbar.setOnClickListener {
            when (it.id) {
                AppBarBase.getLeftButtonId() -> onBackPressed()
                AppBarBase.getRightButtonId() -> toggleNavigationDrawer()
            }
        }


        binding.categoriesRecyclerview.apply {
            layoutManager = GridLayoutManager(this@StoreActivity, 5)
            adapter = storeCategoriesAdapter

        }


        binding.searchEditText.addTextChangedListener {
            viewModel.updateSearchQuery(it.toString())
            showRemoveQueryButton = !it.isNullOrEmpty()
        }

        binding.searchEditText.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                EventLogger.logClickEvent(
                    AnalyticsConstant.Domain.BUSINESS,
                    AnalyticsConstant.Label.SHOP_CATEGORIES_SEARCH,
                    "search in " + getStoreCategoryName(viewModel.category.value)
                )
            }
            v.performClick()
        }


        binding.storeRecyclerview.apply {
            layoutManager = LinearLayoutManager(this@StoreActivity)
            adapter = storeAdapter
        }

        binding.storeSwiperefreshlayout.setOnRefreshListener {
            viewModel.refreshStores()
        }

        binding.searchImageView.setOnClickListener {
            if (showRemoveQueryButton) binding.searchEditText.setText("")
        }


        binding.eventViewPager.apply {

            currentItem = Int.MAX_VALUE / 2
            adapter = storeEventPagerAdapter

            addItemDecoration(
                HorizontalMarginItemDecoration(
                    this@StoreActivity,
                    R.dimen.view_pager_item_margin
                )
            )
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) {

                }

                override fun onPageSelected(position: Int) {

                }

                override fun onPageScrollStateChanged(state: Int) {
                    if (state == ViewPager.SCROLL_STATE_DRAGGING) {
                        startAutoScroll()
                    }
                }
            })
        }

        binding.root.post {
            val rect = Rect()
            window!!.decorView.getWindowVisibleDisplayFrame(rect)
            val statusBarHeight = rect.top

            binding.containerScrollView.setOnScrollChangeListener { v, _, scrollY, _, oldScrollY ->
                val oldScrollRatio =
                    oldScrollY.toFloat() / (binding.containerScrollView.getChildAt(0).height - (binding.root.height - binding.callvanMainLayout.getChildAt(0).height - statusBarHeight))
                val scrollRatio =
                    scrollY.toFloat() / (binding.containerScrollView.getChildAt(0).height - (binding.root.height - binding.callvanMainLayout.getChildAt(0).height - statusBarHeight))
                if (scrollRatio >= 0.7 && oldScrollRatio < 0.7) {
                    EventLogger.logScrollEvent(
                        AnalyticsConstant.Domain.BUSINESS,
                        AnalyticsConstant.Label.SHOP_CATEGORIES,
                        "scroll in " + getStoreCategoryName(viewModel.category.value)
                    )
                }
            }
        }
    }

    private val runnable = object : Runnable {
        override fun run() {
            var currentPosition = binding.eventViewPager.currentItem
            val itemCount = binding.eventViewPager.adapter?.itemCount ?: 0

            currentPosition = if (currentPosition >= itemCount - 1) 0 else currentPosition + 1

            binding.eventViewPager.setCurrentItem(currentPosition, true)
            viewPagerHandler.postDelayed(this, viewPagerDelayTime)
        }
    }

    private fun startAutoScroll() {
        viewPagerHandler.removeCallbacks(runnable)
        viewPagerHandler.postDelayed(runnable, viewPagerDelayTime)
    }

    private fun initViewModel() {
        viewModel.refreshStores()

        observeLiveData(viewModel.isLoading) {
            binding.storeSwiperefreshlayout.isRefreshing = it
        }

        observeLiveData(viewModel.storeEvents) {
            storeEventPagerAdapter.submitList(it)
            binding.eventViewPager.isGone = it.isNullOrEmpty()
        }

        observeLiveData(viewModel.storeCategories) {
            storeCategoriesAdapter.submitList(it.drop(1))
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.stores.collect {
                    storeAdapter.submitList(it)
                }
            }
        }
    }


    private fun getStoreCategoryName(category: StoreCategory?): String {
        return when (category) {
            StoreCategory.Chicken -> getString(R.string.chicken)
            StoreCategory.Pizza -> getString(R.string.pizza)
            StoreCategory.DOSIRAK -> getString(R.string.dorisak)
            StoreCategory.PorkFeet -> getString(R.string.pork_feet)
            StoreCategory.Chinese -> getString(R.string.chinese)
            StoreCategory.NormalFood -> getString(R.string.normal_food)
            StoreCategory.Cafe -> getString(R.string.cafe)
            StoreCategory.BeautySalon -> getString(R.string.beauty_salon)
            StoreCategory.Etc -> getString(R.string.etc)
            StoreCategory.All, null -> getString(R.string.see_all)
        }
    }

    override fun onPause() {
        super.onPause()
        viewPagerHandler.removeCallbacks(runnable)
    }

    override fun onResume() {
        super.onResume()
        startAutoScroll()
    }
}