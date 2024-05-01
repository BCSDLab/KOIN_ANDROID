package `in`.koreatech.koin.ui.store.activity

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import `in`.koreatech.koin.R
import `in`.koreatech.koin.core.appbar.AppBarBase
import `in`.koreatech.koin.core.util.dataBinding
import `in`.koreatech.koin.core.viewpager.HorizontalMarginItemDecoration
import `in`.koreatech.koin.core.viewpager.ScaledViewPager2Transformation
import `in`.koreatech.koin.databinding.StoreActivityMainBinding
import `in`.koreatech.koin.domain.model.store.StoreCategory
import `in`.koreatech.koin.domain.model.store.toStoreCategory
import `in`.koreatech.koin.ui.navigation.KoinNavigationDrawerActivity
import `in`.koreatech.koin.ui.navigation.state.MenuState
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
    private val viewModel by viewModels<StoreViewModel>()

    private val storeDetailContract = registerForActivityResult(StoreDetailActivityContract()) {

    }

    private val viewPagerHandler = Handler(Looper.getMainLooper())
    private val viewPagerDelayTime = 3000L

    private val storeAdapter = StoreRecyclerAdapter().apply {
        setOnItemClickListener {
            storeDetailContract.launch(it.uid)
        }
    }

    private val storeEventPagerAdapter = StoreEventPagerAdapter().apply {
        setOnItemClickListener {
            storeDetailContract.launch(it.shop_id)
        }
    }

    private var isSearchMode: Boolean = false
        set(value) {
            if (value) showSoftKeyboard()
            else hideSoftKeyboard()
            binding.categoryConstraintLayout.isVisible = !value
            field = value
        }

    private var showRemoveQueryButton : Boolean = false
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


        handleCategoryClickEvent()
        initViewModel()
        initView()
        startAutoScroll()

        val initStoreCategory =
            intent.extras?.getInt(StoreActivityContract.STORE_CATEGORY)?.toStoreCategory()
        viewModel.setCategory(initStoreCategory)
    }

    override fun onBackPressed() {
        if (binding.searchEditText.hasFocus()) {
            binding.searchEditText.clearFocus()
            return
        }
        super.onBackPressed()
    }

    private fun initView(){
        binding.koinBaseAppbar.setOnClickListener {
            when (it.id) {
                AppBarBase.getLeftButtonId() -> onBackPressed()
                AppBarBase.getRightButtonId() -> toggleNavigationDrawer()
            }
        }

        binding.searchEditText.addTextChangedListener {
            viewModel.updateSearchQuery(it.toString())
            showRemoveQueryButton = isSearchMode && !it.isNullOrEmpty()
        }

        binding.searchEditText.setOnFocusChangeListener { v, hasFocus ->
            isSearchMode = hasFocus
        }

        binding.storeRecyclerview.apply {
            layoutManager = LinearLayoutManager(this@StoreActivity)
            adapter = storeAdapter
        }

        binding.storeSwiperefreshlayout.setOnRefreshListener {
            viewModel.refreshStores()
        }

        binding.searchImageView.setOnClickListener {
            if(showRemoveQueryButton) binding.searchEditText.setText("")
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
        viewModel.getStoreEvents()

        observeLiveData(viewModel.isLoading) {
            binding.storeSwiperefreshlayout.isRefreshing = it
        }

        observeLiveData(viewModel.storeEvents){
            storeEventPagerAdapter.submitList(it)
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.stores.collect {
                    storeAdapter.submitList(it)
                }
            }
        }


        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.category.collect {
                    handleCategorySelection(it)
                }
            }
        }
    }

    private fun handleCategoryClickEvent() {
        binding.storeCategoryChicken.setCategoryOnClick(StoreCategory.Chicken)
        binding.storeCategoryChickenTextview.setCategoryOnClick(StoreCategory.Chicken)

        binding.storeCategoryPizza.setCategoryOnClick(StoreCategory.Pizza)
        binding.storeCategoryPizzaTextview.setCategoryOnClick(StoreCategory.Pizza)

        binding.storeCategoryDosirak.setCategoryOnClick(StoreCategory.DOSIRAK)
        binding.storeCategoryDosirakTextview.setCategoryOnClick(StoreCategory.DOSIRAK)

        binding.storeCategoryPorkFeet.setCategoryOnClick(StoreCategory.PorkFeet)
        binding.storeCategoryPorkFeetTextview.setCategoryOnClick(StoreCategory.PorkFeet)

        binding.storeCategoryChinese.setCategoryOnClick(StoreCategory.Chinese)
        binding.storeCategoryChineseTextview.setCategoryOnClick(StoreCategory.Chinese)

        binding.storeCategoryNormal.setCategoryOnClick(StoreCategory.NormalFood)
        binding.storeCategoryNormalTextview.setCategoryOnClick(StoreCategory.NormalFood)

        binding.storeCategoryCafe.setCategoryOnClick(StoreCategory.Cafe)
        binding.storeCategoryCafeTextview.setCategoryOnClick(StoreCategory.Cafe)

        binding.storeCategoryHair.setCategoryOnClick(StoreCategory.BeautySalon)
        binding.storeCategoryHairTextview.setCategoryOnClick(StoreCategory.BeautySalon)

        binding.storeCategoryEtc.setCategoryOnClick(StoreCategory.Etc)
        binding.storeCategoryEtcTextview.setCategoryOnClick(StoreCategory.Etc)
    }

    private fun handleCategorySelection(category: StoreCategory?) {
        binding.storeCategoryChickenTextview.setCategorySelected(category == StoreCategory.Chicken)
        binding.storeCategoryPizzaTextview.setCategorySelected(category == StoreCategory.Pizza)
        binding.storeCategoryDosirakTextview.setCategorySelected(category == StoreCategory.DOSIRAK)
        binding.storeCategoryPorkFeetTextview.setCategorySelected(category == StoreCategory.PorkFeet)
        binding.storeCategoryChineseTextview.setCategorySelected(category == StoreCategory.Chinese)
        binding.storeCategoryNormalTextview.setCategorySelected(category == StoreCategory.NormalFood)
        binding.storeCategoryCafeTextview.setCategorySelected(category == StoreCategory.Cafe)
        binding.storeCategoryHairTextview.setCategorySelected(category == StoreCategory.BeautySalon)
        binding.storeCategoryEtcTextview.setCategorySelected(category == StoreCategory.Etc)
    }

    private fun View.setCategoryOnClick(category: StoreCategory) {
        setOnClickListener {
            binding.searchEditText.clearFocus()
            viewModel.setCategory(category)
        }
    }

    private fun TextView.setCategorySelected(isSelected: Boolean) {
        setTextColor(
            ContextCompat.getColor(
                context,
                if (isSelected) R.color.colorAccent else R.color.black
            )
        )
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