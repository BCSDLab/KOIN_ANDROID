package `in`.koreatech.koin.ui.developer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.domain.usecase.setting.GetDeveloperSettingUseCase
import `in`.koreatech.koin.domain.usecase.setting.SetDeveloperSettingUseCase
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class DeveloperSettingViewModel @Inject constructor(
    private val getDeveloperSettingUseCase: GetDeveloperSettingUseCase,
    private val setDeveloperSettingUseCase: SetDeveloperSettingUseCase
) : ViewModel() {

    fun setDeveloperSetting(key: String, value: Boolean) = viewModelScope.launch {
        setDeveloperSettingUseCase(key, value)
        getDeveloperSetting(key)
    }

    fun getDeveloperSetting(key: String) = viewModelScope.launch {
        getDeveloperSettingUseCase(key)
    }
}
