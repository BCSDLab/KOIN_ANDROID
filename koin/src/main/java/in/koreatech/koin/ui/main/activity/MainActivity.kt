package `in`.koreatech.koin.ui.main.activity

import `in`.koreatech.koin.R
import `in`.koreatech.koin.core.appbar.AppBarBase
import `in`.koreatech.koin.core.recyclerview.RecyclerViewClickListener
import `in`.koreatech.koin.core.util.dataBinding
import `in`.koreatech.koin.core.viewpager.HorizontalMarginItemDecoration
import `in`.koreatech.koin.core.viewpager.ScaleCardPagerTransformer
import `in`.koreatech.koin.core.viewpager.ScaledViewPager2Transformation
import `in`.koreatech.koin.data.util.localized
import `in`.koreatech.koin.databinding.ActivityMainBinding
import `in`.koreatech.koin.domain.model.dining.Dining
import `in`.koreatech.koin.domain.model.dining.DiningType
import `in`.koreatech.koin.ui.main.StoreCategoryRecyclerAdapter
import `in`.koreatech.koin.ui.main.adapter.BusPagerAdapter
import `in`.koreatech.koin.ui.main.adapter.DiningTypeAdapter
import `in`.koreatech.koin.ui.main.state.DiningTypeUiState
import `in`.koreatech.koin.ui.main.viewmodel.MainActivityViewModel
import `in`.koreatech.koin.ui.navigation.KoinNavigationDrawerActivity
import `in`.koreatech.koin.ui.navigation.state.MenuState
import `in`.koreatech.koin.util.ext.observeLiveData
import android.graphics.Point
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import dagger.hilt.android.AndroidEntryPoint
import `in`.koreatech.koin.domain.util.DiningUtil
import `in`.koreatech.koin.domain.util.ext.arrange
import `in`.koreatech.koin.domain.util.ext.typeFilter
import `in`.koreatech.koin.ui.store.contract.StoreActivityContract

@AndroidEntryPoint
class MainActivity : KoinNavigationDrawerActivity() {
    override val menuState = MenuState.Main
    private val binding by dataBinding<ActivityMainBinding>(R.layout.activity_main)
    override val screenTitle = "코인 - 메인"
    private val mainActivityViewModel by viewModels<MainActivityViewModel>()

    private val busPagerAdapter = BusPagerAdapter().apply {
        setOnCardClickListener { callDrawerItem(R.id.navi_item_bus, Bundle()) }
        setOnSwitchClickListener { mainActivityViewModel.switchBusNode() }
    }
    private val diningTypeAdapter = DiningTypeAdapter().apply {
        setOnItemClickListener {
            mainActivityViewModel.setSelectedPosition(it)
        }
    }

    private val storeCategoryRecyclerAdapter = StoreCategoryRecyclerAdapter().apply {
        setRecyclerViewClickListener(object : RecyclerViewClickListener {
            override fun onClick(view: View?, position: Int) {
                gotoStoreActivity(position)
            }

            override fun onLongClick(view: View?, position: Int) {

            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initView()
        initViewModel()
    }

    override fun onResume() {
        super.onResume()
        mainActivityViewModel.updateDining()
    }

    private fun initView() = with(binding) {
        buttonCategory.setOnClickListener {
            toggleNavigationDrawer()
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
            adapter = storeCategoryRecyclerAdapter
        }

//        recyclerViewDiningType.apply {
//            layoutManager =
//                LinearLayoutManager(this@MainActivity, RecyclerView.HORIZONTAL, false)
//            adapter = diningTypeAdapter
//        }

        mainSwipeRefreshLayout.setOnRefreshListener {
            mainActivityViewModel.updateDining()
        }

//        diningContainer.setOnClickListener {
//            callDrawerItem(R.id.navi_item_dining)
//        }

        DiningUtil.diningPlace.forEach { place ->
            tabDining.addTab(tabDining.newTab().setText(place))
        }
    }

    private fun initViewModel() = with(mainActivityViewModel) {
        observeLiveData(isLoading) {
            binding.mainSwipeRefreshLayout.isRefreshing = it
        }

        observeLiveData(diningData) {
            updateDining(it, selectedPosition.value ?: 0)
        }

        observeLiveData(selectedPosition) { position ->
            diningData.value?.let { list ->
                updateDining(list, position)
            }
        }

        observeLiveData(selectedType) {
            binding.textViewDiningTime.text = it.localized(this@MainActivity)
        }

        observeLiveData(busTimer) {
            busPagerAdapter.setBusTimerItems(it)
        }
    }

    private fun gotoStoreActivity(position: Int) {
        val bundle = Bundle()
        bundle.putInt(StoreActivityContract.STORE_CATEGORY, position)
        callDrawerItem(R.id.navi_item_store, bundle)
    }

    fun updateDining(list: List<Dining>, position: Int) {
        val diningType = DiningUtil.getCurrentType()
        val diningArranged = list
            .typeFilter(diningType)
            .arrange()

        diningTypeAdapter.submitList(
            diningArranged
                .mapIndexed { index, dining ->
                    DiningTypeUiState(
                        dining.place,
                        index == position
                    )
                }
        )

//        if (list.isEmpty() || position >= diningArranged.size) {
//            binding.viewEmptyDining.emptyDiningListFrameLayout.isVisible = true
//            return
//        }

//        binding.viewEmptyDining.emptyDiningListFrameLayout.isVisible = false
//
//        listOf(
//            binding.textViewCardDiningMenu0,
//            binding.textViewCardDiningMenu2,
//            binding.textViewCardDiningMenu4,
//            binding.textViewCardDiningMenu6,
//            binding.textViewCardDiningMenu8,
//            binding.textViewCardDiningMenu1,
//            binding.textViewCardDiningMenu3,
//            binding.textViewCardDiningMenu5,
//            binding.textViewCardDiningMenu7,
//            binding.textViewCardDiningMenu9
//        ).zip(diningArranged[position].menu).forEach { (textView, menu) ->
//            textView.text = menu
//        }

//        val isSoldOut = diningArranged[position].soldoutAt.isNotEmpty()
//        val isChanged = diningArranged[position].changedAt.isNotEmpty()
//        with (binding.textViewDiningStatus) {
//            when {
//                isSoldOut -> {
//                    text = context.getString(R.string.dining_soldout)
//                    setTextColor(ContextCompat.getColor(context, R.color.dining_soldout_text))
//                    background = ContextCompat.getDrawable(context, R.drawable.dining_soldout_fill_radius_4)
//                }
//                isChanged -> {
//                    text = context.getString(R.string.dining_changed)
//                    setTextColor(ContextCompat.getColor(context, R.color.dining_changed_text))
//                    background = ContextCompat.getDrawable(context, R.drawable.dining_changed_fill_radius_4)
//                }
//                else -> {
//                    visibility = View.INVISIBLE
//                }
//            }
//        }
    }
}
