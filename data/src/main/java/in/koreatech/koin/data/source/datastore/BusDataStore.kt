package `in`.koreatech.koin.data.source.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class BusDataStore @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {

    suspend fun getLastShownNoticeId(): Int {
        return dataStore.data.catch {
            emit(emptyPreferences())
        }.map { preferences ->
            preferences[KEY_LAST_SHOWN_NOTICE_ID]
        }.first()!!
    }

    suspend fun saveLastShownNoticeId(id: Int) {
        dataStore.edit { preferences ->
            preferences[KEY_LAST_SHOWN_NOTICE_ID] = id
        }
    }

    companion object {
        private val KEY_LAST_SHOWN_NOTICE_ID = intPreferencesKey("last_shown_notice_id")
    }
}