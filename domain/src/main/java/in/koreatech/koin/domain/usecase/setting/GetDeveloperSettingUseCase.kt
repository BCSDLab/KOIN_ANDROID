package `in`.koreatech.koin.domain.usecase.setting

import `in`.koreatech.koin.domain.repository.SettingRepository
import javax.inject.Inject

class GetDeveloperSettingUseCase @Inject constructor(
    private val settingRepository: SettingRepository
) {
    suspend operator fun invoke(key: String): Boolean {
        return settingRepository.getDeveloperSettingValue(key)
    }
}
