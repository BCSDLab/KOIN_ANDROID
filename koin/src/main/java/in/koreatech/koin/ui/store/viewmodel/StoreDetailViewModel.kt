package `in`.koreatech.koin.ui.store.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.core.viewmodel.BaseViewModel
import `in`.koreatech.koin.domain.model.store.ShopMenus
import `in`.koreatech.koin.domain.model.store.Store
import `in`.koreatech.koin.domain.model.store.StoreMenu
import `in`.koreatech.koin.domain.model.store.StoreWithMenu
import `in`.koreatech.koin.domain.usecase.store.GetRecommendStoresUseCase
import `in`.koreatech.koin.domain.usecase.store.GetShopMenusUseCase
import `in`.koreatech.koin.domain.usecase.store.GetStoreWithMenuUseCase
import javax.inject.Inject

@HiltViewModel
class StoreDetailViewModel @Inject constructor(
    private val getStoreWithMenuUseCase: GetStoreWithMenuUseCase,
    private val getRecommendStoresUseCase: GetRecommendStoresUseCase,
    private val getShopMenusUseCase: GetShopMenusUseCase
) : BaseViewModel() {
    val store: LiveData<StoreWithMenu> get() = _store
    private val _store = MutableLiveData<StoreWithMenu>()
    val categories: LiveData<StoreMenu> get() = _categories
    private var _categories = MutableLiveData<StoreMenu>()
    val storeMenu: LiveData<List<ShopMenus>> get() = _storeMenu
    private val _storeMenu = MutableLiveData<List<ShopMenus>>()
    val recommendStores: LiveData<List<Store>?> get() = _recommendStores
    private val _recommendStores = MutableLiveData<List<Store>?>()

    fun getStoreWithMenu(storeId: Int) = viewModelScope.launchWithLoading {
        getStoreWithMenuUseCase(storeId).also { store ->
            _store.value = store
            _recommendStores.value = getRecommendStoresUseCase(store)
        }
    }

    fun getShopMenus(storeId: Int) = viewModelScope.launchWithLoading {
        getShopMenusUseCase(storeId).also { shop ->
            _categories.value = shop
            _storeMenu.value = if (shop.menuCategories?.isEmpty() == true) {
                emptyList()
            } else {
                shop.menuCategories?.first()?.menus ?: emptyList()
            }
        }
    }
}