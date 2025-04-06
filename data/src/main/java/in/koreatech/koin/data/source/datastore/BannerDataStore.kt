package `in`.koreatech.koin.data.source.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import javax.inject.Inject
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class BannerDataStore @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    suspend fun getBannerRefusalDate(): Int {
        return dataStore.data.catch {
            emit(emptyPreferences())
        }.map { preferences ->
            preferences[KEY_BANNER_REFUSAL_DATE] ?: 0
        }.first()
    }

    suspend fun saveBannerRefusalDate(date: Int) {
        dataStore.edit { preferences ->
            preferences[KEY_BANNER_REFUSAL_DATE] = date
        }
    }

    companion object {
        private val KEY_BANNER_REFUSAL_DATE = intPreferencesKey("banner_refusal_date")
    }
}
