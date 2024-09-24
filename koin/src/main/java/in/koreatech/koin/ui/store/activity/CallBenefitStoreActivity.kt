package `in`.koreatech.koin.ui.store.activity

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.viewModels
import androidx.core.view.isGone
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import dagger.hilt.android.AndroidEntryPoint
import `in`.koreatech.koin.R
import `in`.koreatech.koin.core.activity.ActivityBase
import `in`.koreatech.koin.core.analytics.EventLogger
import `in`.koreatech.koin.core.constant.AnalyticsConstant
import `in`.koreatech.koin.core.util.dataBinding
import `in`.koreatech.koin.core.viewpager.HorizontalMarginItemDecoration
import `in`.koreatech.koin.databinding.ActivityCallBenefitStoreMainBinding
import `in`.koreatech.koin.domain.model.store.StoreCategory
import `in`.koreatech.koin.domain.model.store.StoreSorter
import `in`.koreatech.koin.ui.store.adapter.StoreEventPagerAdapter
import `in`.koreatech.koin.ui.store.adapter.StoreRecyclerAdapter
import `in`.koreatech.koin.ui.store.contract.StoreDetailActivityContract
import `in`.koreatech.koin.ui.store.viewmodel.StoreBenefitViewModel
import `in`.koreatech.koin.ui.store.viewmodel.StoreViewModel
import `in`.koreatech.koin.util.ext.observeLiveData
import `in`.koreatech.koin.util.ext.withLoading
import kotlinx.coroutines.launch
@AndroidEntryPoint
class CallBenefitStoreActivity : ActivityBase() {
    override val screenTitle: String
        get() = "전화주문혜택"
    private val binding: ActivityCallBenefitStoreMainBinding by dataBinding<ActivityCallBenefitStoreMainBinding>(R.layout.activity_call_benefit_store_main)
    private val viewModel by viewModels<StoreViewModel>()
    private val benefitViewModel by viewModels<StoreBenefitViewModel>()
    private val viewPagerHandler = Handler(Looper.getMainLooper())
    private val viewPagerDelayTime = 10000L
    private val storeDetailContract = registerForActivityResult(StoreDetailActivityContract()) {
    }
    private val storeAdapter = StoreRecyclerAdapter().apply {
        setOnItemClickListener {
            storeDetailContract.launch(Pair(it.uid, getStoreCategoryName(StoreCategory.All)))
        }
    }
    private val storeEventPagerAdapter = StoreEventPagerAdapter().apply {
        setOnItemClickListener {
            EventLogger.logClickEvent(
                AnalyticsConstant.Domain.BUSINESS,
                AnalyticsConstant.Label.SHOP_CATEGORIES_EVENT,
                it.shopName
            )
            storeDetailContract.launch(Pair(it.shopId, getStoreCategoryName(viewModel.category.value)))
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initViewModel()
        initView()

    }

    private fun initView(){
        with(binding) {
            storeRecyclerview.apply {
                layoutManager = LinearLayoutManager(this@CallBenefitStoreActivity)
                adapter = storeAdapter
            }


            eventViewPager.apply {
                currentItem = Int.MAX_VALUE / 2
                adapter = storeEventPagerAdapter
                addItemDecoration(
                    HorizontalMarginItemDecoration(
                        this@CallBenefitStoreActivity,
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

                    override fun onPageScrollStateChanged(state: Int) {
                        if (state == ViewPager.SCROLL_STATE_DRAGGING) {
                            startAutoScroll()
                        }
                    }
                })
            }
        }
    }

    private fun initViewModel(){
        viewModel.refreshStores()
        withLoading(this, benefitViewModel)
        lifecycleScope.launch{
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                benefitViewModel.storeBenefitCategories.collect{
                    binding.benefitDescription.text= it.benefitCategories.getOrNull(benefitViewModel.categoryId.value)?.detail ?: ""
                }
            }
        }
        lifecycleScope.launch{
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                benefitViewModel.benefitShopList.collect{
                    if(it.shops.isNotEmpty()) {
                        storeAdapter.submitList(it.shops)
                    }
                }
            }
        }

        observeLiveData(viewModel.storeEvents) {
            storeEventPagerAdapter.submitList(it)
            binding.eventViewPager.isGone = it.isNullOrEmpty()
        }

        viewModel.settingStoreSorter(StoreSorter.NONE)
    }

    fun getStoreCategoryName(category: StoreCategory?): String {
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

    override fun onRestart() {
        viewModel.refreshStores()
        super.onRestart()
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

}