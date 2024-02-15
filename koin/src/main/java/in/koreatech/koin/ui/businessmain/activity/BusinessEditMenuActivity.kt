package `in`.koreatech.koin.ui.businessmain.activity

import `in`.koreatech.koin.R
import `in`.koreatech.koin.core.util.dataBinding
import `in`.koreatech.koin.data.entity.MenuItemEntity
import `in`.koreatech.koin.databinding.ActivityBusinessEditMenuBinding
import `in`.koreatech.koin.ui.businessmain.adapter.BusinessEditMenuAdapter
import `in`.koreatech.koin.ui.businessmain.viewmodel.BusinessMainViewModel
import `in`.koreatech.koin.ui.navigation.KoinBusinessNavigationDrawerActivity
import `in`.koreatech.koin.ui.navigation.state.MenuState
import `in`.koreatech.koin.util.ext.observeLiveData
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BusinessEditMenuActivity : KoinBusinessNavigationDrawerActivity() {
    override val menuState = MenuState.BusinessEditMenu
    private val binding by dataBinding<ActivityBusinessEditMenuBinding>(R.layout.activity_business_edit_menu)
    val viewModel: BusinessMainViewModel by viewModels()
    private val menuList = listOf(
        MenuItemEntity(title = "가게정보", imageResource = R.drawable.ic_business_menu_store),
        MenuItemEntity(title = "매출관리", imageResource = R.drawable.ic_business_menu_sales_management),
        MenuItemEntity(title = "메뉴관리", imageResource = R.drawable.ic_business_menu_management),
        MenuItemEntity(title = "주문관리", imageResource = R.drawable.ic_business_menu_order_management),
        MenuItemEntity(title = "주변상점", imageResource = R.drawable.ic_business_menu_near_store),
        MenuItemEntity(title = "버스/교통", imageResource = R.drawable.ic_business_menu_bus),
        MenuItemEntity(title = "메뉴얼 다시보기", imageResource = R.drawable.ic_business_menu_manual)
    )
    private val businessEditMenuAdapter: BusinessEditMenuAdapter by lazy {
        BusinessEditMenuAdapter(menuList.toMutableList()) {
            viewModel.updateSelectedItems(businessEditMenuAdapter.getSelectedItems())
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
            recyclerViewEditBusinessMenu.layoutManager = GridLayoutManager(this@BusinessEditMenuActivity,3)
            recyclerViewEditBusinessMenu.adapter = businessEditMenuAdapter

            buttonEdit.setOnClickListener {
                val selectedItems = viewModel.getSelectedItems()
                viewModel.saveMenuItems(selectedItems)
                val resultIntent = Intent()
                setResult(Activity.RESULT_OK, resultIntent)
                finish()
            }
            buttonCategory.setOnClickListener {
                toggleBusinessNavigationDrawer()
            }
        }

    }

    private fun initViewModel() {
        with(viewModel) {
            observeLiveData(menuItems) {
                updateMenuList(it)
                businessEditMenuAdapter.notifyDataSetChanged()
            }
        }
    }

    private fun updateMenuList(storedItems: List<MenuItemEntity>) {
        for (storedItem in storedItems) {
            val index = menuList.indexOfFirst { it.title == storedItem.title }
            if (index != -1) {
                menuList[index].isSelected = true
            }
        }
    }
}