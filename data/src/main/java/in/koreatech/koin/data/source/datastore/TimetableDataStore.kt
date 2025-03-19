package `in`.koreatech.koin.data.source.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TimetableDataStore @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    fun getString(key: String): Flow<String> =
        dataStore.data.map { preferences ->
            preferences[stringPreferencesKey(key)] ?: ""
        }

    suspend fun putString(
        key: String,
        value: String
    ) {
        dataStore.edit { preferences ->
            preferences[stringPreferencesKey(key)] = value
        }
    }
}
