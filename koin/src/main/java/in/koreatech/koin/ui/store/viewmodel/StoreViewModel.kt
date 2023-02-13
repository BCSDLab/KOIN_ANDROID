package `in`.koreatech.koin.ui.store.viewmodel

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.core.viewmodel.BaseViewModel
import `in`.koreatech.koin.domain.model.store.Store
import `in`.koreatech.koin.domain.model.store.StoreCategory
import `in`.koreatech.koin.domain.usecase.store.GetStoresUseCase
import `in`.koreatech.koin.domain.usecase.store.InvalidateStoresUseCase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StoreViewModel @Inject constructor(
    private val getStoresUseCase: GetStoresUseCase,
    private val invalidateStoresUseCase: InvalidateStoresUseCase
) : BaseViewModel() {
    private val search = MutableStateFlow("")
    private val refreshEvent = MutableSharedFlow<Unit>()
    private val _category = MutableStateFlow<StoreCategory?>(null)
    private val _stores = MutableStateFlow<List<Store>>(emptyList())

    val category: StateFlow<StoreCategory?> = _category.asStateFlow()
    val stores: StateFlow<List<Store>> = _stores.asStateFlow()

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
}