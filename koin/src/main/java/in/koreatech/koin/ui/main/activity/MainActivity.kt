package `in`.koreatech.koin.ui.main.activity

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint
import `in`.koreatech.koin.R
import `in`.koreatech.koin.core.recyclerview.RecyclerViewClickListener
import `in`.koreatech.koin.core.util.dataBinding
import `in`.koreatech.koin.core.viewpager.HorizontalMarginItemDecoration
import `in`.koreatech.koin.data.util.todayOrTomorrow
import `in`.koreatech.koin.databinding.ActivityMainBinding
import `in`.koreatech.koin.domain.model.dining.DiningPlace
import `in`.koreatech.koin.ui.main.StoreCategoryRecyclerAdapter
import `in`.koreatech.koin.ui.main.adapter.BusPagerAdapter
import `in`.koreatech.koin.ui.main.adapter.DiningContainerViewPager2Adapter
import `in`.koreatech.koin.ui.main.viewmodel.MainActivityViewModel
import `in`.koreatech.koin.ui.navigation.KoinNavigationDrawerActivity
import `in`.koreatech.koin.ui.navigation.state.MenuState
import `in`.koreatech.koin.ui.store.contract.StoreActivityContract
import `in`.koreatech.koin.util.ext.observeLiveData

@AndroidEntryPoint
class MainActivity : KoinNavigationDrawerActivity() {
    override val menuState = MenuState.Main
    private val binding by dataBinding<ActivityMainBinding>(R.layout.activity_main)
    override val screenTitle = "코인 - 메인"
    private val viewModel by viewModels<MainActivityViewModel>()

    private val busPagerAdapter = BusPagerAdapter().apply {
        setOnCardClickListener { callDrawerItem(R.id.navi_item_bus, Bundle()) }
        setOnSwitchClickListener { viewModel.switchBusNode() }
    }
    private val diningContainerAdapter by lazy { DiningContainerViewPager2Adapter(this) }

    private val storeCategoryRecyclerAdapter = StoreCategoryRecyclerAdapter().apply {
        setRecyclerViewClickListener(object : RecyclerViewClickListener {
            override fun onClick(view: View?, position: Int) {
                gotoStoreActivity(position)
            }

            override fun onLongClick(view: View?, position: Int) {

            }
        })
    }

    private val requestNotificationPermissionResult = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
        // handle POST_NOTIFICATION permission
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initView()
        initViewModel()
        getToken()
    }

    override fun onResume() {
        super.onResume()
        viewModel.updateDining()
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
        }
    }

    private fun getToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {}
            val token = task.result
            Log.e("device_token", "token : $token")
        }
    }

    private fun checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
                getToken()
            } else {
                requestNotificationPermissionResult.launch(android.Manifest.permission.POST_NOTIFICATIONS)
            }
        } else {
            getToken()
        }
    }

    private fun gotoStoreActivity(position: Int) {
        val bundle = Bundle()
        bundle.putInt(StoreActivityContract.STORE_CATEGORY, position)
        callDrawerItem(R.id.navi_item_store, bundle)
    }
}
