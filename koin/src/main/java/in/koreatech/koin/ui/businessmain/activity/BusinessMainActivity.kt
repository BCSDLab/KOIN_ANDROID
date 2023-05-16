package `in`.koreatech.koin.ui.businessmain.activity

import `in`.koreatech.koin.R
import `in`.koreatech.koin.core.util.dataBinding
import `in`.koreatech.koin.data.sharedpreference.BusinessMenuSharedPreferences
import `in`.koreatech.koin.databinding.ActivityBusinessMainBinding
import `in`.koreatech.koin.ui.businessmain.BusinessMainStartBusinessDialog
import `in`.koreatech.koin.ui.businessmain.adapter.BusinessMenuAdapter
import `in`.koreatech.koin.ui.businessmain.viewmodel.BusinessMainViewModel
import `in`.koreatech.koin.ui.businessmain.MenuItem
import `in`.koreatech.koin.ui.navigation.KoinBusinessNavigationDrawerActivity
import `in`.koreatech.koin.ui.navigation.state.MenuState
import `in`.koreatech.koin.util.ext.observeLiveData
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BusinessMainActivity : KoinBusinessNavigationDrawerActivity() {
    override val menuState = MenuState.BusinessMain
    private val binding by dataBinding<ActivityBusinessMainBinding>(R.layout.activity_business_main)
    val viewModel: BusinessMainViewModel by viewModels()
    private val selectedMenuItemsPreferences by lazy { BusinessMenuSharedPreferences(this) }
    private val menuList = listOf(
        MenuItem(title = "가게정보", imageResource = R.drawable.ic_business_menu_store),
        MenuItem(title = "매출관리", imageResource = R.drawable.ic_business_menu_sales_management),
        MenuItem(title = "메뉴관리", imageResource = R.drawable.ic_business_menu_management),
        MenuItem(title = "주문관리", imageResource = R.drawable.ic_business_menu_order_management),
        MenuItem(title = "주변상점", imageResource = R.drawable.ic_business_menu_near_store),
        MenuItem(title = "버스/교통", imageResource = R.drawable.ic_business_menu_bus),
        MenuItem(title = "메뉴얼 다시보기", imageResource = R.drawable.ic_business_menu_manual)
    )

    private val businessMenuAdapter: BusinessMenuAdapter by lazy {
        val storedSelectedItems = selectedMenuItemsPreferences.loadSelectedItems()

        if (storedSelectedItems.isEmpty()) {
            BusinessMenuAdapter(listOf(
                MenuItem(title = "가게정보", imageResource = R.drawable.ic_business_menu_store),
                MenuItem(title = "매출관리", imageResource = R.drawable.ic_business_menu_sales_management),
                MenuItem(title = "메뉴관리", imageResource = R.drawable.ic_business_menu_management)
            ).toMutableList()) {}
        } else {
            val selectedMenuItems = getSelectedMenuItems(storedSelectedItems)
            BusinessMenuAdapter(selectedMenuItems.toMutableList()) {}
        }
    }

    private val businessEditMenuActivityLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                val selectedItems: List<Int> =
                    data?.getIntegerArrayListExtra("selectedItems") ?: emptyList()
                selectedMenuItemsPreferences.saveSelectedItems(selectedItems)
                businessMenuAdapter.setMenuList(getSelectedMenuItems(selectedItems))
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initView()
        initViewModel()
    }

    private fun initView() {
        with(binding) {
            vm = viewModel
            recyclerViewBusinessMenu.layoutManager = GridLayoutManager(this@BusinessMainActivity, 3)
            recyclerViewBusinessMenu.adapter = businessMenuAdapter

            buttonEdit.setOnClickListener {
                goToBusinessEditMenuActivity()
            }
            buttonCategory.setOnClickListener {
                toggleBusinessNavigationDrawer()
            }
            switchStartStore.setOnCheckedChangeListener { p0, isChecked ->
                if (isChecked) {
                    BusinessMainStartBusinessDialog().show(supportFragmentManager,
                        "BusinessMainStartBusinessDialog")
                    materialCardViewTodaySales.visibility = View.VISIBLE
                } else {
                    materialCardViewTodaySales.visibility = View.GONE
                }
            }
            businessMainSwipeRefreshLayout.setOnRefreshListener {
                businessMainSwipeRefreshLayout.isRefreshing = false
            }
            imageViewBusinessStatusRefresh.setOnClickListener {
            }
        }
    }

    private fun initViewModel() {
        with(viewModel) {
            observeLiveData(currentDateTime) {
                binding.textViewCurrentDate.text = it
            }
        }

    }

    fun goToBusinessEditMenuActivity() {
        val intent = Intent(this, BusinessEditMenuActivity::class.java)
        intent.putIntegerArrayListExtra("storedItems",
            ArrayList(selectedMenuItemsPreferences.loadSelectedItems()))
        businessEditMenuActivityLauncher.launch(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.stopUpdatingDateTime()
    }

    override fun onStop() {
        super.onStop()
        viewModel.stopUpdatingDateTime()
    }

    override fun onStart() {
        super.onStart()
        viewModel.startUpdatingDateTime()
    }

    fun getSelectedMenuItems(selectedImageResources: List<Int>): List<MenuItem> {
        return menuList.filter { it.imageResource in selectedImageResources }
    }
}