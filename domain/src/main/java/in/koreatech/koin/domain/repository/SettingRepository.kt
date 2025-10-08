package `in`.koreatech.koin.domain.repository

interface SettingRepository {
    suspend fun getDeveloperSettingValue(key: String): Boolean
    suspend fun setDeveloperSettingValue(key: String, value: Boolean)
}
