package `in`.koreatech.koin.ui.store.activity

import android.os.Bundle
import android.view.MotionEvent
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
import `in`.koreatech.koin.core.analytics.EventLogger
import `in`.koreatech.koin.core.appbar.AppBarBase
import `in`.koreatech.koin.core.constant.AnalyticsConstant
import `in`.koreatech.koin.core.util.dataBinding
import `in`.koreatech.koin.databinding.StoreActivityMainBinding
import `in`.koreatech.koin.domain.model.store.StoreCategory
import `in`.koreatech.koin.domain.model.store.toStoreCategory
import `in`.koreatech.koin.ui.navigation.KoinNavigationDrawerActivity
import `in`.koreatech.koin.ui.navigation.state.MenuState
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

        handleCategoryClickEvent()
        initViewModel()

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

    private fun initViewModel() {
        viewModel.refreshStores()

        observeLiveData(viewModel.isLoading) {
            binding.storeSwiperefreshlayout.isRefreshing = it
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

    private fun View.setCategoryOnClick(category: StoreCategory) {
        setOnClickListener {
            binding.searchEditText.clearFocus()
            viewModel.setCategory(category)

            val eventValue = getStoreCategoryName(viewModel.category.value)

            EventLogger.logClickEvent(
                AnalyticsConstant.Domain.BUSINESS,
                AnalyticsConstant.Label.SHOP_CATEGORIES,
                eventValue
            )
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
}

