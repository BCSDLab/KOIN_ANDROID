package `in`.koreatech.koin.ui.store.activity

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
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
import androidx.viewpager2.widget.ViewPager2
import dagger.hilt.android.AndroidEntryPoint
import `in`.koreatech.koin.R
import `in`.koreatech.koin.core.analytics.EventAction
import `in`.koreatech.koin.core.analytics.EventExtra
import `in`.koreatech.koin.core.analytics.EventLogger
import `in`.koreatech.koin.core.analytics.EventUtils
import `in`.koreatech.koin.core.appbar.AppBarBase
import `in`.koreatech.koin.core.constant.AnalyticsConstant
import `in`.koreatech.koin.core.onboarding.ArrowDirection
import `in`.koreatech.koin.core.onboarding.OnboardingManager
import `in`.koreatech.koin.core.onboarding.OnboardingType
import `in`.koreatech.koin.core.util.dataBinding
import `in`.koreatech.koin.core.viewpager.HorizontalMarginItemDecoration
import `in`.koreatech.koin.databinding.StoreActivityMainBinding
import `in`.koreatech.koin.domain.model.store.StoreEvent
import `in`.koreatech.koin.domain.model.store.StoreSorter
import `in`.koreatech.koin.ui.navigation.KoinNavigationDrawerTimeActivity
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
import `in`.koreatech.koin.util.ext.statusBarHeight
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.properties.Delegates

@AndroidEntryPoint
class StoreActivity : KoinNavigationDrawerTimeActivity() {
    override val menuState = MenuState.Store
    private var currentTime by Delegates.notNull<Long>()
    private var categoryElapsedTime by Delegates.notNull<Long>()
    private var storeElapsedTime by Delegates.notNull<Long>()

    private val binding by dataBinding<StoreActivityMainBinding>(R.layout.store_activity_main)
    override val screenTitle = "상점"
    private val viewModel by viewModels<StoreViewModel>()

    @Inject
    lateinit var onboardingManager: OnboardingManager

    private var eventListSize by Delegates.notNull<Int>()

    fun interface StoreCategoryFactory {
        fun getCurrentCategory(): String
    }
    private val storeDetailContract = registerForActivityResult(StoreDetailActivityContract {
        viewModel.category.value?.let { viewModel.category.value?.name } ?: "Unknown"
    }) {

    }

    private val viewPagerHandler = Handler(Looper.getMainLooper())
    private val viewPagerDelayTime = 4000L

    private val storeAdapter = StoreRecyclerAdapter().apply {
        setOnItemClickListener {
            storeElapsedTime = System.currentTimeMillis() - currentTime

            storeDetailContract.launch(Triple(it.uid, viewModel.category.value?.name, false))
            val categoryName = viewModel.category.value?.name

            if(categoryName != null){
                EventLogger.logClickEvent(
                    EventAction.BUSINESS,
                    AnalyticsConstant.Label.SHOP_CLICK,
                    it.name,
                    EventExtra(AnalyticsConstant.PREVIOUS_PAGE, categoryName),
                    EventExtra(AnalyticsConstant.CURRENT_PAGE, it.name),
                    EventExtra(AnalyticsConstant.DURATION_TIME, getElapsedTimeAndReset().toString())
                )
            }
        }
    }

    private val storeEventPagerAdapter = StoreEventPagerAdapter().apply {
        setOnItemClickListener {
            EventLogger.logClickEvent(
                EventAction.BUSINESS,
                AnalyticsConstant.Label.SHOP_CATEGORIES_EVENT,
                it.shopName
            )
            storeDetailContract.launch(Triple(it.shopId, viewModel.category.value?.name, false))
        }
    }

    private val storeCategoriesAdapter = StoreCategoriesRecyclerAdapter().apply {
        setOnItemClickListener {
            val previous = viewModel.category.value?.name

            viewModel.setCategory(it)
            binding.searchEditText.text.clear()
            val current = viewModel.category.value?.name

            if (current != null && previous != null) {
                EventLogger.logClickEvent(
                    EventAction.BUSINESS,
                    AnalyticsConstant.Label.SHOP_CATEGORIES,
                    current,
                    EventExtra(AnalyticsConstant.PREVIOUS_PAGE, previous),
                    EventExtra(AnalyticsConstant.CURRENT_PAGE, current),
                    EventExtra(AnalyticsConstant.DURATION_TIME, getElapsedTimeAndReset().toString())
                )
            }
            preCategories = it
            currentTime = System.currentTimeMillis()
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
        initSortingTooltip()

        val initStoreCategory =
            intent.extras?.getInt(StoreActivityContract.STORE_CATEGORY)
        storeCategoriesAdapter.selectPosition =
            intent.extras?.getInt(StoreActivityContract.STORE_CATEGORY)?.minus(2)
        viewModel.setCategory(initStoreCategory)

        storeCategoriesAdapter.initCategory(initStoreCategory)
    }

    override fun onBackPressed() {
        if (binding.searchEditText.hasFocus()) {
            binding.searchEditText.clearFocus()
            return
        }
        super.onBackPressed()
    }

    private fun initView() {
        currentTime = System.currentTimeMillis()
        binding.koinBaseAppbar.setOnClickListener {
            when (it.id) {
                AppBarBase.getLeftButtonId() -> onBackPressed()
                AppBarBase.getRightButtonId() -> toggleNavigationDrawer()
            }
        }


        binding.categoriesRecyclerview.apply {
            layoutManager = GridLayoutManager(this@StoreActivity, 6)
            adapter = storeCategoriesAdapter
        }


        binding.searchEditText.addTextChangedListener {
            viewModel.updateSearchQuery(it.toString())
            showRemoveQueryButton = !it.isNullOrEmpty()
        }

        binding.searchEditText.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                EventLogger.logClickEvent(
                    EventAction.BUSINESS,
                    AnalyticsConstant.Label.SHOP_CATEGORIES_SEARCH,
                    "search in " + viewModel.category.value?.name
                )
            }
            v.performClick()
        }

        binding.leftEventArrow.setOnClickListener {
            val currentPosition = binding.eventViewPager.currentItem
            val previousPosition = if (currentPosition - 1 >= 1 ) currentPosition - 1 else eventListSize - 2
            binding.eventViewPager.setCurrentItem(previousPosition, true)
            startAutoScroll()
        }

        binding.rightEventArrow.setOnClickListener {
            val currentPosition = binding.eventViewPager.currentItem
            val nextPosition = if(currentPosition + 1 <= eventListSize - 2) currentPosition + 1 else 1
            binding.eventViewPager.setCurrentItem(nextPosition, true)
            startAutoScroll()
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
                    if(state == ViewPager2.SCROLL_STATE_IDLE){
                        if(binding.eventViewPager.currentItem == eventListSize - 1){
                            binding.eventViewPager.setCurrentItem(1, false)
                            binding.eventPageCounterTextView.text = "1/${eventListSize - 2}"
                        }

                        if(binding.eventViewPager.currentItem == 0){
                            binding.eventViewPager.setCurrentItem(eventListSize - 2, false)
                            binding.eventPageCounterTextView.text = "${eventListSize - 2}/${eventListSize - 2}"
                        }
                        binding.eventPageCounterTextView.text = "${binding.eventViewPager.currentItem}/${eventListSize - 2}"
                        startAutoScroll()
                    }
                }
            })
        }

        binding.root.post {
            val statusBarHeight = this.statusBarHeight

            binding.containerScrollView.setOnScrollChangeListener { v, _, scrollY, _, oldScrollY ->
                val oldScrollRatio =
                    oldScrollY.toFloat() / (binding.containerScrollView.getChildAt(0).height - (binding.root.height - binding.callvanMainLayout.getChildAt(
                        0
                    ).height - statusBarHeight))
                val scrollRatio =
                    scrollY.toFloat() / (binding.containerScrollView.getChildAt(0).height - (binding.root.height - binding.callvanMainLayout.getChildAt(0).height - statusBarHeight))
                if (EventUtils.didCrossedScrollThreshold(oldScrollRatio, scrollRatio)) {
                    EventLogger.logScrollEvent(
                        EventAction.BUSINESS,
                        AnalyticsConstant.Label.SHOP_CATEGORIES,
                        "scroll in " + viewModel.category.value?.name
                    )
                }
            }
        }

        with(binding) {
            storeManyReviewCheckbox.setOnClickListener {

                if (storeManyReviewCheckbox.isChecked) {
                    EventLogger.logClickEvent(
                        EventAction.BUSINESS,
                        AnalyticsConstant.Label.SHOP_CAN,
                        "check_review_"+viewModel.category.value?.let { viewModel.category.value?.name }
                    )
                    storeManyReviewCheckbox.setTextColor(
                        ContextCompat.getColor(
                            this@StoreActivity,
                            R.color.blue_alpha20
                        )
                    )
                    viewModel.settingStoreSorter(StoreSorter.COUNT)

                    storeHighRatingCheckbox.isChecked = false
                    storeHighRatingCheckbox.setTextColor(
                        ContextCompat.getColor(
                            this@StoreActivity,
                            R.color.gray15
                        )
                    )
                } else {
                    storeManyReviewCheckbox.setTextColor(
                        ContextCompat.getColor(
                            this@StoreActivity,
                            R.color.gray15
                        )
                    )
                    viewModel.settingStoreSorter(StoreSorter.NONE)
                }
            }

            storeHighRatingCheckbox.setOnClickListener {
                if (storeHighRatingCheckbox.isChecked) {
                    EventLogger.logClickEvent(
                        EventAction.BUSINESS,
                        AnalyticsConstant.Label.SHOP_CAN,
                        "check_star_"+viewModel.category.value?.let { viewModel.category.value?.name }
                    )
                    storeHighRatingCheckbox.setTextColor(
                        ContextCompat.getColor(
                            this@StoreActivity,
                            R.color.blue_alpha20
                        )
                    )
                    viewModel.settingStoreSorter(StoreSorter.RATING)

                    storeManyReviewCheckbox.isChecked = false
                    storeManyReviewCheckbox.setTextColor(
                        ContextCompat.getColor(
                            this@StoreActivity,
                            R.color.gray15
                        )
                    )
                } else {
                    storeHighRatingCheckbox.setTextColor(
                        ContextCompat.getColor(
                            this@StoreActivity,
                            R.color.gray15
                        )
                    )
                    viewModel.settingStoreSorter(StoreSorter.NONE)
                }
            }

            storeIsOperatingCheckbox.setOnClickListener {
                storeIsOperatingCheckbox.setTextColor(
                    if (storeIsOperatingCheckbox.isChecked) {
                        EventLogger.logClickEvent(
                            EventAction.BUSINESS,
                            AnalyticsConstant.Label.SHOP_CAN,
                            "check_open_"+viewModel.category.value?.let { viewModel.category.value?.name }
                        )
                        ContextCompat.getColor(
                        this@StoreActivity,
                        R.color.blue_alpha20
                    )}
                    else ContextCompat.getColor(this@StoreActivity, R.color.gray15)
                )

                viewModel.filterStoreIsOpen(storeIsOperatingCheckbox.isChecked)
            }

            storeIsDeliveryCheckbox.setOnClickListener {
                storeIsDeliveryCheckbox.setTextColor(
                    if (storeIsDeliveryCheckbox.isChecked){
                        EventLogger.logClickEvent(
                            EventAction.BUSINESS,
                            AnalyticsConstant.Label.SHOP_CAN,
                            "check_delivery_"+viewModel.category.value?.let { viewModel.category.value?.name }
                        )
                        ContextCompat.getColor(
                        this@StoreActivity,
                        R.color.blue_alpha20
                    )}
                    else ContextCompat.getColor(this@StoreActivity, R.color.gray15)
                )

                viewModel.filterStoreIsDelivery(storeIsDeliveryCheckbox.isChecked)
            }
        }
    }

    private val runnable = object : Runnable {
        override fun run() {
            var currentPosition = binding.eventViewPager.currentItem
            currentPosition = if (currentPosition >= eventListSize - 1) 0 else currentPosition + 1

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
            if (!it.isNullOrEmpty()) {
                val augmentedList = mutableListOf<StoreEvent>().apply {
                    add(it.last())
                    addAll(it)
                    add(it.first())
                }
                binding.eventPageCounterTextView.text = "1/${augmentedList.size - 2}"
                eventListSize = augmentedList.size
                storeEventPagerAdapter.submitList(augmentedList)

                binding.eventViewPager.setCurrentItem(1, false)
            }
            binding.eventViewPager.isGone = it.isNullOrEmpty()
        }

        observeLiveData(viewModel.storeCategoryList) {
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

    private fun initSortingTooltip() {
        with(onboardingManager) {
            showOnboardingTooltipIfNeeded(
                type = OnboardingType.REVIEW_SORTING,
                view = binding.storeManyReviewCheckbox,
                arrowPosition = 0.1f,
                arrowDirection = ArrowDirection.BOTTOM
            )
        }
    }

    override fun onRestart() {
        viewModel.refreshStores()
        super.onRestart()
    }

    override fun onPause() {
        super.onPause()
        viewPagerHandler.removeCallbacks(runnable)
    }

    override fun onResume() {
        currentTime = System.currentTimeMillis()
        super.onResume()
        startAutoScroll()
    }
}