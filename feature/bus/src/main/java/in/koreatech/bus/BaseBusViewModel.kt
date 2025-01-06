package `in`.koreatech.bus

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

abstract class BaseBusViewModel: ViewModel() {

    private val _refreshToggle = MutableStateFlow(false)
    protected val refreshToggle = _refreshToggle.asStateFlow()

    open fun refresh() {
        _refreshToggle.value = !_refreshToggle.value
    }
}