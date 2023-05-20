package `in`.koreatech.koin.ui.business.mystore.viewmodel

import android.util.Log
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.core.viewmodel.BaseViewModel
import `in`.koreatech.koin.domain.model.business.mystore.MyStore
import `in`.koreatech.koin.domain.model.store.Store
import `in`.koreatech.koin.domain.usecase.business.mystore.GetMyStoreUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyStoreViewModel @Inject constructor(
    private val getMyStoreUseCase: GetMyStoreUseCase
): BaseViewModel() {
    private val _stores = MutableStateFlow<List<MyStore>>(emptyList())
    val stores: StateFlow<List<MyStore>> = _stores.asStateFlow()

    fun getStores(){
        viewModelScope.launch {
            _stores.value = getMyStoreUseCase.testUseCase()
        }
    }
}
