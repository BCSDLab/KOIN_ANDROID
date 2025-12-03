package `in`.koreatech.koin.ui.developer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.koreatech.koin.core.developer.DeveloperOption
import `in`.koreatech.koin.core.developer.DeveloperOptionUtil
import `in`.koreatech.koin.core.developer.developerOptionList
import `in`.koreatech.koin.domain.usecase.setting.GetDeveloperSettingUseCase
import `in`.koreatech.koin.domain.usecase.setting.SetDeveloperSettingUseCase
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class DeveloperSettingViewModel @Inject constructor(
    private val getDeveloperSettingUseCase: GetDeveloperSettingUseCase,
    private val setDeveloperSettingUseCase: SetDeveloperSettingUseCase
) : ViewModel() {

    init {
        getDeveloperSettings()
    }

    fun setDeveloperSetting(key: DeveloperOption, value: Boolean) = viewModelScope.launch {
        setDeveloperSettingUseCase(key.key, value)
        DeveloperOptionUtil.setDeveloperOption(key, value)
    }

    fun getDeveloperSettings() = viewModelScope.launch {
        developerOptionList.forEach {
            DeveloperOptionUtil.setDeveloperOption(it, getDeveloperSettingUseCase(it.key))
        }
    }
}
