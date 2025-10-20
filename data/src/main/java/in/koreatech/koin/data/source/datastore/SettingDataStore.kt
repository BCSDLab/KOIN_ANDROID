package `in`.koreatech.koin.data.source.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import javax.inject.Inject
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class SettingDataStore @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    suspend fun getDeveloperSettingValue(key: String): Boolean {
        return dataStore.data.catch {
            emit(emptyPreferences())
        }.map { preferences ->
            preferences[booleanPreferencesKey(key)] ?: false
        }.first()
    }

    suspend fun setDeveloperSettingValue(key: String, value: Boolean) {
        dataStore.edit { preferences ->
            preferences[booleanPreferencesKey(key)] = value
        }
    }
}
