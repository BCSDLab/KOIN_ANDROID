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
import android.util.Log
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
    private val selectedMenuItemsPreferences by lazy{ BusinessMenuSharedPreferences(this)}
    private val businessMenuAdapter: BusinessMenuAdapter by lazy {
        if (selectedMenuItemsPreferences.loadSelectedItems().isEmpty()) {
            BusinessMenuAdapter(listOf(MenuItem(title = "가게정보", imageResource = R.drawable.ic_business_menu_store)).toMutableList()){}
        } else BusinessMenuAdapter(selectedMenuItemsPreferences.loadSelectedItems().toMutableList()) {
        }
    }

    private val businessEditMenuActivityLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                val selectedItems: List<MenuItem> =
                    data?.getParcelableArrayListExtra("selectedItems") ?: emptyList()
                selectedMenuItemsPreferences.saveSelectedItems(selectedItems)
                businessMenuAdapter.setMenuList(selectedItems)
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
            recyclerViewBusinessMenu.layoutManager = GridLayoutManager(this@BusinessMainActivity,3)
            recyclerViewBusinessMenu.adapter = businessMenuAdapter

            buttonEdit.setOnClickListener {
                goToBusinessEditMenuActivity()
            }
            buttonCategory.setOnClickListener {
                toggleBusinessNavigationDrawer()
            }
            switchStartStore.setOnCheckedChangeListener { p0, isChecked ->
                if (isChecked) {
                    BusinessMainStartBusinessDialog().show(supportFragmentManager, "BusinessMainStartBusinessDialog")
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
        intent.putParcelableArrayListExtra("storedItems", ArrayList(selectedMenuItemsPreferences.loadSelectedItems()))
        businessEditMenuActivityLauncher.launch(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e("test","onDestroy")
        viewModel.stopUpdatingDateTime()
    }

    override fun onStop() {
        super.onStop()
        Log.e("test","onStop")
        viewModel.stopUpdatingDateTime()
    }

    override fun onStart() {
        super.onStart()
        Log.e("test","onStart")
        viewModel.startUpdatingDateTime()
    }
}