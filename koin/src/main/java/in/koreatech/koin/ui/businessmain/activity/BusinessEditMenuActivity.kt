package `in`.koreatech.koin.ui.businessmain.activity

import `in`.koreatech.koin.R
import `in`.koreatech.koin.core.util.dataBinding
import `in`.koreatech.koin.databinding.ActivityBusinessEditMenuBinding
import `in`.koreatech.koin.ui.businessmain.adapter.BusinessEditMenuAdapter
import `in`.koreatech.koin.ui.businessmain.viewmodel.BusinessMainViewModel
import `in`.koreatech.koin.ui.businessmain.MenuItem
import `in`.koreatech.koin.ui.navigation.KoinBusinessNavigationDrawerActivity
import `in`.koreatech.koin.ui.navigation.state.MenuState
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
        MenuItem(title = "가게정보", imageResource = R.drawable.ic_business_menu_store),
        MenuItem(title = "매출관리", imageResource = R.drawable.ic_business_menu_sales_management),
        MenuItem(title = "메뉴관리", imageResource = R.drawable.ic_business_menu_management),
        MenuItem(title = "주문관리", imageResource = R.drawable.ic_business_menu_order_management),
        MenuItem(title = "주변상점", imageResource = R.drawable.ic_business_menu_near_store),
        MenuItem(title = "버스/교통", imageResource = R.drawable.ic_business_menu_bus),
        MenuItem(title = "메뉴얼 다시보기", imageResource = R.drawable.ic_business_menu_manual)
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
        updateMenuListFromIntent()
        with(binding) {
            recyclerViewEditBusinessMenu.layoutManager = GridLayoutManager(this@BusinessEditMenuActivity,3)
            recyclerViewEditBusinessMenu.adapter = businessEditMenuAdapter

            buttonEdit.setOnClickListener {
                val selectedItems = viewModel.getSelectedItems()
                val resultIntent = Intent()
                resultIntent.putParcelableArrayListExtra("selectedItems", ArrayList(selectedItems))
                setResult(Activity.RESULT_OK, resultIntent)
                finish()
            }
            buttonCategory.setOnClickListener {
                toggleBusinessNavigationDrawer()
            }
        }

    }

    private fun initViewModel() {
    }

    private fun updateMenuListFromIntent() {
        if (intent.hasExtra("storedItems")) {
            val storedItems: List<MenuItem> =
                intent.getParcelableArrayListExtra("storedItems") ?: emptyList()
            for (storedItem in storedItems) {
                val index = menuList.indexOfFirst { it.title == storedItem.title }
                if (index != -1) {
                    menuList[index].isSelected = true
                }
            }
        }
    }

}