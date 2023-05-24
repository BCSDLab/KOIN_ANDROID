package `in`.koreatech.koin.ui.businessmain.activity

import `in`.koreatech.koin.R
import `in`.koreatech.koin.core.util.dataBinding
import `in`.koreatech.koin.databinding.ActivityBusinessMainBinding
import `in`.koreatech.koin.ui.businessmain.BusinessMainStartBusinessDialog
import `in`.koreatech.koin.ui.businessmain.adapter.BusinessMenuAdapter
import `in`.koreatech.koin.ui.businessmain.viewmodel.BusinessMainViewModel
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
    private lateinit var businessMenuAdapter: BusinessMenuAdapter

    private val businessEditMenuActivityLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                viewModel.loadMenuItems()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initView()
        initViewModel()
    }

    private fun initView() {
        viewModel.loadMenuItems()
        with(binding) {
            vm = viewModel
            recyclerViewBusinessMenu.layoutManager = GridLayoutManager(this@BusinessMainActivity, 3)

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
            observeLiveData(menuItems) { menuItems ->
                if (::businessMenuAdapter.isInitialized) {
                    businessMenuAdapter.setMenuList(menuItems)
                } else {
                    businessMenuAdapter = BusinessMenuAdapter(menuItems.toMutableList()) {}
                    binding.recyclerViewBusinessMenu.adapter = businessMenuAdapter
                }
            }
        }

    }

    fun goToBusinessEditMenuActivity() {
        val intent = Intent(this, BusinessEditMenuActivity::class.java)
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
}
