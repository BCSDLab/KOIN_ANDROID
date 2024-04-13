package `in`.koreatech.koin.ui.store.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.core.viewmodel.BaseViewModel
import `in`.koreatech.koin.domain.model.dining.Dining
import `in`.koreatech.koin.domain.model.store.NeedSignUpStoreInfo
import `in`.koreatech.koin.domain.model.store.Store
import `in`.koreatech.koin.domain.model.store.StoreCategory
import `in`.koreatech.koin.domain.model.store.StoreEvent
import `in`.koreatech.koin.domain.usecase.store.GetStoreEventUseCase
import `in`.koreatech.koin.domain.usecase.store.GetStoresUseCase
import `in`.koreatech.koin.domain.usecase.store.InvalidateStoresUseCase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StoreViewModel @Inject constructor(
    private val getStoresUseCase: GetStoresUseCase,
    private val invalidateStoresUseCase: InvalidateStoresUseCase,
    private val getStoreEventUseCase: GetStoreEventUseCase
) : BaseViewModel() {
    private val search = MutableStateFlow("")
    private val refreshEvent = MutableSharedFlow<Unit>()
    private val _category = MutableStateFlow<StoreCategory?>(null)
    private val _stores = MutableStateFlow<List<Store>>(emptyList())
    private val _store = MutableStateFlow<Store?>(null)
    private val _needToProceedStoreInfo = MutableSharedFlow<NeedSignUpStoreInfo>()

    private val _storeEvents = MutableLiveData<List<StoreEvent>?>(emptyList())
    val storeEvents: LiveData<List<StoreEvent>?> get() = _storeEvents

    val category: StateFlow<StoreCategory?> = _category.asStateFlow()
    val stores: StateFlow<List<Store>> = _stores.asStateFlow()
    val store: StateFlow<Store?> = _store.asStateFlow()
    val needToProceedStoreInfo = _needToProceedStoreInfo.asSharedFlow()

    init {
        viewModelScope.launch {
            search
                .debounce(100)
                .combine(category) { search, category ->
                    search to category
                }.combine(refreshEvent) { value, _ ->
                    value
                }
                .map { (search, category) ->
                    getStoresUseCase(
                        category = category,
                        search = search
                    )
                }
                .collectLatest {
                    _isLoading.value = false
                    _stores.value = it
                }
        }
    }

    fun updateSearchQuery(query: String) {
        search.value = query
    }

    fun setCategory(storeCategory: StoreCategory?) {
        if(category.value == storeCategory) {
            _category.value = null
        } else {
            _category.value = storeCategory
        }
    }

    fun refreshStores() = viewModelScope.launch {
        invalidateStoresUseCase()
        refreshEvent.emit(Unit)
    }

    fun clickStoreItem(item: Store) {
        viewModelScope.launch {
            _store.update { item }
        }
    }

    fun setNeedToProceedStoreInfo(check: Boolean, storeName: String, storeId: Int, storeNumber: String) {
        viewModelScope.launch {
            _needToProceedStoreInfo.emit(NeedSignUpStoreInfo(check,  storeName, storeId))
        }
    }

    fun getStoreEvents(){
        viewModelScope.launch {
            _storeEvents.value = getStoreEventUseCase()
        }
    }
}