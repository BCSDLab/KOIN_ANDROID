package `in`.koreatech.koin.ui.store.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.core.viewmodel.BaseViewModel
import `in`.koreatech.koin.domain.model.store.NeedSignUpStoreInfo
import `in`.koreatech.koin.domain.model.store.ShopSearchRelatedList
import `in`.koreatech.koin.domain.model.store.Store
import `in`.koreatech.koin.domain.model.store.StoreCategories
import `in`.koreatech.koin.domain.model.store.StoreCategory
import `in`.koreatech.koin.domain.model.store.StoreEvent
import `in`.koreatech.koin.domain.model.store.StoreSorter
import `in`.koreatech.koin.domain.usecase.store.GetStoreCategoriesUseCase
import `in`.koreatech.koin.domain.usecase.store.GetStoreEventUseCase
import `in`.koreatech.koin.domain.usecase.store.GetStoresUseCase
import `in`.koreatech.koin.domain.usecase.store.InvalidateStoresUseCase
import `in`.koreatech.koin.domain.usecase.store.SearchStoreUseCase
import `in`.koreatech.koin.domain.usecase.store.search.GetRelatedStoreUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StoreViewModel @Inject constructor(
    private val getStoresUseCase: GetStoresUseCase,
    private val searchStoreUseCase: SearchStoreUseCase,
    private val invalidateStoresUseCase: InvalidateStoresUseCase,
    private val getStoreEventUseCase: GetStoreEventUseCase,
    private val getStoreCategoriesUseCase: GetStoreCategoriesUseCase,
    private val getStoreWithMenuUseCase: GetRelatedStoreUseCase,
) : BaseViewModel() {
    private val _isSearchMode = MutableStateFlow<Boolean>(false)
    private val _search = MutableStateFlow("")
    private val refreshEvent = MutableSharedFlow<Unit>()
    private val _category = MutableStateFlow<StoreCategories?>(StoreCategories(0, "", ""))
    private val _stores = MutableStateFlow<List<Store>>(emptyList())
    private val _store = MutableStateFlow<Store?>(null)
    private val _needToProceedStoreInfo = MutableSharedFlow<NeedSignUpStoreInfo>()

    private val _storeEvents = MutableLiveData<List<StoreEvent>?>(emptyList())
    private val _searchRelated = MutableLiveData<ShopSearchRelatedList>()

    val storeEvents: LiveData<List<StoreEvent>?> get() = _storeEvents
    val searchRelated get() = _searchRelated

    val isSearchMode: StateFlow<Boolean> = _isSearchMode
    val search: StateFlow<String> = _search.asStateFlow()
    val category: StateFlow<StoreCategories?> = _category.asStateFlow()
    val stores: StateFlow<List<Store>> = _stores.asStateFlow()
    val store: StateFlow<Store?> = _store.asStateFlow()
    val needToProceedStoreInfo = _needToProceedStoreInfo.asSharedFlow()

    private val _categoryPosition = MutableLiveData(-1)
    val categoryPosition: LiveData<Int> get() = _categoryPosition

    private val _storeCategoryList = MutableLiveData<List<StoreCategories>>(emptyList())
    val storeCategoryList: LiveData<List<StoreCategories>> get() = _storeCategoryList

    private val _storeSorter = MutableLiveData<StoreSorter>()
    val storeSorter: LiveData<StoreSorter> get() = _storeSorter

    private val _isOperating = MutableLiveData<Boolean>(false)
    val isOperating: LiveData<Boolean> get() = _isOperating

    private val _isDelivery = MutableLiveData<Boolean>(false)
    val isDelivery: LiveData<Boolean> get() = _isDelivery

    init {
        _storeSorter.value = StoreSorter.COUNT
        getStoreCategories()
        getStoreEvents()
        //changeCategory()
        searchStore()

    }

    fun searchResult() {
        viewModelScope.launch {
            search.collectLatest {
                refreshStores()
            }
        }
    }

    fun updateSearchQuery(query: String) {
        _search.value = query
    }

    fun setCategory(categoryPosition: Int?) {
        if(_categoryPosition.value == categoryPosition) {
            _categoryPosition.value = 0
            _category.value = _storeCategoryList.value?.get(0)
        } else {
            _categoryPosition.value = categoryPosition
            _category.value = categoryPosition?.let { _storeCategoryList.value?.get(it) }
        }
    }

    private fun changeCategory() {
        viewModelScope.launch {
            category
                .combine(refreshEvent) { value, _ ->
                    value
                }
                .map {
                    getStoresUseCase(
                        category = it,
                        storeSorter = _storeSorter.value,
                        isOperating = _isOperating.value,
                        isDelivery = _isDelivery.value
                    )
                }
                .collectLatest {
                    _isLoading.value = false
                    _stores.value = it
                }
        }
    }

    private fun searchStore() {
        viewModelScope.launch {
            search
                .debounce(100)
                .combine(category) { search, category ->
                    search to category
                }.combine(refreshEvent) { value, _ ->
                    value
                }
                .map { (search, category) ->
                    searchStoreUseCase(
                        search = search,
                        category = category,
                        storeSorter = _storeSorter.value,
                        isOperating = _isOperating.value,
                        isDelivery = _isDelivery.value
                    )
                }
                .collectLatest {
                    _isLoading.value = false
                    _stores.value = it
                }
        }
    }

    fun refreshStores() = viewModelScope.launch {
        delay(100)
        invalidateStoresUseCase()
        refreshEvent.emit(Unit)
    }

    fun clickStoreItem(item: Store) {
        viewModelScope.launch {
            _store.update { item }
        }
    }

    fun setNeedToProceedStoreInfo(
        check: Boolean,
        storeName: String,
        storeId: Int,
        storeNumber: String
    ) {
        viewModelScope.launch {
            _needToProceedStoreInfo.emit(NeedSignUpStoreInfo(check, storeName, storeId))
        }
    }

    fun settingStoreSorter(sorter: StoreSorter) {
        _storeSorter.value = sorter
        viewModelScope.launch {
            invalidateStoresUseCase()
            refreshEvent.emit(Unit)
        }
    }

    fun filterStoreIsOpen(isOpen: Boolean) {
        _isOperating.value = isOpen
        viewModelScope.launch {
            invalidateStoresUseCase()
            refreshEvent.emit(Unit)
        }
    }

    fun filterStoreIsDelivery(isDelivery: Boolean) {
        _isDelivery.value = isDelivery
        viewModelScope.launch {
            invalidateStoresUseCase()
            refreshEvent.emit(Unit)
        }
    }

    private fun getStoreEvents(){
        viewModelScope.launch {
            _storeEvents.value = getStoreEventUseCase()
        }
    }

    private fun getStoreCategories() {
        viewModelScope.launchWithLoading {
            _storeCategoryList.value = getStoreCategoriesUseCase()
        }
    }

    fun getRelatedStore() {
        viewModelScope.launch {
            _searchRelated.value = getStoreWithMenuUseCase(_search.value)
        }
    }
}