package `in`.koreatech.koin.ui.businessmain.viewmodel

import `in`.koreatech.koin.R
import `in`.koreatech.koin.core.viewmodel.BaseViewModel
import `in`.koreatech.koin.data.database.MenuItemDao
import `in`.koreatech.koin.data.entity.MenuItemEntity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class BusinessMainViewModel @Inject constructor(
    private val menuItemDao: MenuItemDao,
) : BaseViewModel() {
    private val _selectedMenuItems = MutableLiveData<List<MenuItemEntity>>()
    val selectedMenuItems: LiveData<List<MenuItemEntity>>
        get() = _selectedMenuItems

    private val _currentDateTime = MutableLiveData<String>()
    val currentDateTime: LiveData<String> get() = _currentDateTime

    private val _menuItems = MutableLiveData<List<MenuItemEntity>>()
    val menuItems: LiveData<List<MenuItemEntity>>
        get() = _menuItems

    var job: Job? = null

    init {
        startUpdatingDateTime()
    }

    fun startUpdatingDateTime() {
        job = viewModelScope.launch {
            while (true) {
                _currentDateTime.value = getCurrentDateTime()
                delay(60_000)
            }
        }
    }

    fun stopUpdatingDateTime() {
        job?.cancel()
    }

    fun updateSelectedItems(selectedItems: List<MenuItemEntity>) {
        _selectedMenuItems.value = selectedItems
    }

    fun getSelectedItems(): List<MenuItemEntity> {
        return selectedMenuItems.value?.filter { it.isSelected } ?: emptyList()
    }

    private fun getCurrentDateTime(): String {
        val currentDate = Calendar.getInstance().time
        val dateFormatter = SimpleDateFormat("yyyy-MM-dd a hh:mm", Locale.getDefault())
        return dateFormatter.format(currentDate)
    }

    fun loadMenuItems() {
        viewModelScope.launch {
            val loadedMenuItems = menuItemDao.loadAllMenuItems()
            if (loadedMenuItems.isEmpty()) {
                val defaultMenuItems = listOf(
                    MenuItemEntity(title = "가게정보", imageResource = R.drawable.ic_business_menu_store),
                    MenuItemEntity(title = "매출관리", imageResource = R.drawable.ic_business_menu_sales_management),
                    MenuItemEntity(title = "메뉴관리", imageResource = R.drawable.ic_business_menu_management)
                )
                menuItemDao.saveMenuItems(defaultMenuItems)
                _menuItems.value = defaultMenuItems
            } else {
                _menuItems.value = loadedMenuItems
            }
        }
    }


    fun saveMenuItems(items: List<MenuItemEntity>) {
        viewModelScope.launch {
            menuItemDao.deleteAllMenuItems()
            menuItemDao.saveMenuItems(items)
        }
    }
}