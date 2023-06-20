package `in`.koreatech.koin.ui.store.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.core.viewmodel.BaseViewModel
import `in`.koreatech.koin.domain.model.store.Store
import `in`.koreatech.koin.domain.model.store.StoreMenu
import `in`.koreatech.koin.domain.usecase.store.GetRecommendStoresUseCase
import `in`.koreatech.koin.domain.usecase.store.GetStoreWithMenuUseCase
import javax.inject.Inject

@HiltViewModel
class StoreDetailViewModel @Inject constructor(
    private val getStoreWithMenuUseCase: GetStoreWithMenuUseCase,
    private val getRecommendStoresUseCase: GetRecommendStoresUseCase
) : BaseViewModel() {
    private val _store = MutableLiveData<Store>()
    private val _storeMenu = MutableLiveData<List<StoreMenu>>()
    private val _recommendStores = MutableLiveData<List<Store>?>()

    val store : LiveData<Store> get() = _store
    val storeMenu : LiveData<List<StoreMenu>> get() = _storeMenu
    val recommendStores : LiveData<List<Store>?> get() = _recommendStores

    fun getStoreWithMenu(storeId: Int) = viewModelScope.launchWithLoading {
        getStoreWithMenuUseCase(storeId).also { (store, menu) ->
            _store.value = store
            _storeMenu.value = menu
            _recommendStores.value = getRecommendStoresUseCase(store)
        }
    }
}