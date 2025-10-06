package `in`.koreatech.koin.data.source.local

import `in`.koreatech.koin.data.source.datastore.SettingDataStore
import javax.inject.Inject

class SettingLocalDataSource @Inject constructor(
    private val settingDataStore: SettingDataStore
) {
    suspend fun getDeveloperSettingValue(key: String): Boolean {
        return settingDataStore.getDeveloperSettingValue(key)
    }

    suspend fun setDeveloperSettingValue(key: String, value: Boolean) {
        settingDataStore.setDeveloperSettingValue(key, value)
    }
}
