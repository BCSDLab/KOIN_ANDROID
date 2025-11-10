package `in`.koreatech.koin.feature.store

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.domain.usecase.setting.GetDeveloperSettingUseCase
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class StoreViewModel @Inject constructor(
    private val getDeveloperSettingUseCase: GetDeveloperSettingUseCase
) : ViewModel() {
    private val _currentRoute = MutableStateFlow(0)
    val currentRoute = _currentRoute.asStateFlow()

    private val _deliveryDeveloperSetting = MutableStateFlow(false)
    val deliveryDeveloperSetting get() = _deliveryDeveloperSetting

    init {
        getDeveloperSettings()
    }

    fun setCurrentRoute(route: Int) {
        _currentRoute.value = route
    }

    private fun getDeveloperSettings() = viewModelScope.launch {
        _deliveryDeveloperSetting.value = getDeveloperSettingUseCase(DELIVERY_SPRINT)
    }

    companion object {
        const val DELIVERY_SPRINT = "delivery_sprint"
    }
}
