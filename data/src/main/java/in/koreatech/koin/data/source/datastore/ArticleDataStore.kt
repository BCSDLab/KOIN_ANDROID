package `in`.koreatech.koin.data.source.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ArticleDataStore @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {

    private val gson = Gson()

    fun fetchSearchHistory(): Flow<List<String>> {
        return dataStore.data
            .map { preferences ->
                val historyJson = preferences[KEY_SEARCH_HISTORY] ?: ""
                gson.fromJson<List<String>>(historyJson, List::class.java)
            }
    }

    suspend fun saveSearchHistory(keyword: String) {
        dataStore.edit { preferences ->
            val historyJson = preferences[KEY_SEARCH_HISTORY] ?: ""
            val list = gson.fromJson<List<String>>(historyJson, List::class.java).toMutableList()
            list.add(0, keyword)
            preferences[KEY_SEARCH_HISTORY] = gson.toJson(list)
        }
    }

    suspend fun deleteSearchHistory(vararg query: String) {
        dataStore.edit { preferences ->
            val historyJson = preferences[KEY_SEARCH_HISTORY] ?: ""
            val list = gson.fromJson<List<String>>(historyJson, List::class.java).toMutableList()
            query.forEach { list.remove(it) }
            preferences[KEY_SEARCH_HISTORY] = gson.toJson(list)
        }
    }

    companion object {
        private val KEY_SEARCH_HISTORY = stringPreferencesKey("search_history")
    }
}