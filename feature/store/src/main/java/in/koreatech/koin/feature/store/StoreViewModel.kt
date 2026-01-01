package `in`.koreatech.koin.feature.store

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.domain.usecase.setting.GetDeveloperSettingUseCase
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

@HiltViewModel
class StoreViewModel @Inject constructor(
    private val getDeveloperSettingUseCase: GetDeveloperSettingUseCase
) : ViewModel() {
    private val _currentRoute = MutableStateFlow(0)
    val currentRoute = _currentRoute.asStateFlow()

    fun setCurrentRoute(route: Int) {
        _currentRoute.value = route
    }
}
