package `in`.koreatech.koin.ui.developer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.domain.usecase.setting.GetDeveloperSettingUseCase
import `in`.koreatech.koin.domain.usecase.setting.SetDeveloperSettingUseCase
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class DeveloperSettingViewModel @Inject constructor(
    private val getDeveloperSettingUseCase: GetDeveloperSettingUseCase,
    private val setDeveloperSettingUseCase: SetDeveloperSettingUseCase
) : ViewModel() {

    private val _storeDeveloperSetting = MutableStateFlow(false)
    val storeDeveloperSetting get() = _storeDeveloperSetting

    init {
        getDeveloperSettings()
    }

    fun setDeveloperSetting(key: String, value: Boolean) = viewModelScope.launch {
        setDeveloperSettingUseCase(key, value)
        getDeveloperSettings()
    }

    fun getDeveloperSettings() = viewModelScope.launch {
        _storeDeveloperSetting.value = getDeveloperSettingUseCase(STORE_SPRINT)
    }

    companion object {
        const val STORE_SPRINT = "store_sprint"
    }
}
