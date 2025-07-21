package `in`.koreatech.koin.feature.store.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

@HiltViewModel
class StoreViewModel @Inject constructor() : ViewModel() {
    private val _currentRoute = MutableStateFlow(0)
    val currentRoute = _currentRoute.asStateFlow()

    fun setCurrentRoute(route: Int) {
        _currentRoute.value = route
    }
}
