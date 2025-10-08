package `in`.koreatech.koin.domain.usecase.setting

import `in`.koreatech.koin.domain.repository.SettingRepository
import javax.inject.Inject

class SetDeveloperSettingUseCase @Inject constructor(
    private val settingRepository: SettingRepository
) {
    suspend operator fun invoke(key: String, value: Boolean) {
        settingRepository.setDeveloperSettingValue(key, value)
    }
}
