package `in`.koreatech.koin.data.repository

import `in`.koreatech.koin.data.source.local.SettingLocalDataSource
import `in`.koreatech.koin.domain.repository.SettingRepository
import javax.inject.Inject

class SettingRepositoryImpl @Inject constructor(
    private val settingLocalDataSource: SettingLocalDataSource
) : SettingRepository {
    override suspend fun getDeveloperSettingValue(key: String): Boolean {
        return settingLocalDataSource.getDeveloperSettingValue(key)
    }

    override suspend fun setDeveloperSettingValue(key: String, value: Boolean) {
        settingLocalDataSource.setDeveloperSettingValue(key, value)
    }
}
