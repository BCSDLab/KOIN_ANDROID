package `in`.koreatech.koin.data.source.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.google.gson.Gson
import `in`.koreatech.koin.data.constant.WEEK_IN_MILLIS
import `in`.koreatech.koin.domain.model.article.articleNotiContent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
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
                gson.fromJson<List<String>>(historyJson, List::class.java) ?: emptyList()
            }
    }

    suspend fun saveSearchHistory(keyword: String) {
        dataStore.edit { preferences ->
            val historyJson = preferences[KEY_SEARCH_HISTORY] ?: ""
            val list = (gson.fromJson<List<String>>(historyJson, List::class.java) ?: listOf()).toMutableList()
            if (list.contains(keyword)) {
                list.remove(keyword)
            }
            list.add(0, keyword)
            preferences[KEY_SEARCH_HISTORY] = gson.toJson(list)
        }
    }

    suspend fun deleteSearchHistory(query: String) {
        dataStore.edit { preferences ->
            val historyJson = preferences[KEY_SEARCH_HISTORY] ?: ""
            val list = gson.fromJson<List<String>>(historyJson, List::class.java).toMutableList()
            list.remove(query)
            preferences[KEY_SEARCH_HISTORY] = gson.toJson(list)
        }
    }

    suspend fun clearSearchHistory() {
        dataStore.edit { preferences ->
            preferences.remove(KEY_SEARCH_HISTORY)
        }
    }

    suspend fun fetchMyKeyword(): List<String> {
        return dataStore.data.first()[KEY_MY_KEYWORD]?.let {
            gson.fromJson<List<String>>(it, List::class.java) ?: emptyList()
        } ?: emptyList()

    }

    suspend fun saveKeyword(keyword: String) {
        dataStore.edit { preferences ->
            val myKeyword = preferences[KEY_MY_KEYWORD] ?: ""
            val list = (gson.fromJson<List<String>>(myKeyword, List::class.java) ?: listOf()).toMutableList()
            list.add(keyword)
            preferences[KEY_MY_KEYWORD] = gson.toJson(list)
        }
    }

    suspend fun deleteKeyword(keyword: String) {
        dataStore.edit { preferences ->
            val myKeyword = preferences[KEY_MY_KEYWORD] ?: ""
            val list = gson.fromJson<List<String>>(myKeyword, List::class.java).toMutableList()
            list.remove(keyword)
            preferences[KEY_MY_KEYWORD] = gson.toJson(list)
        }
    }

    suspend fun fetchKeywordNotiIndex(): Int {
        return dataStore.data.first()[KEY_NOTI_INDEX] ?: 0
    }

    suspend fun saveKeywordNotiIndex() {
        dataStore.edit { preferences ->
            var notiIndex = preferences[KEY_NOTI_INDEX] ?: 0
            val lastUpdateTime = preferences[KEY_NOTI_LAST_UPDATE] ?: 0L
            val currentTime = System.currentTimeMillis()

            if (currentTime - lastUpdateTime >= WEEK_IN_MILLIS) {
                notiIndex = (notiIndex + 1) % articleNotiContent.size
                preferences[KEY_NOTI_INDEX] = notiIndex
                preferences[KEY_NOTI_LAST_UPDATE] = currentTime
            }
        }
    }

    companion object {
        private val KEY_SEARCH_HISTORY = stringPreferencesKey("search_history")
        private val KEY_MY_KEYWORD = stringPreferencesKey("my_keyword")
        private val KEY_NOTI_INDEX = intPreferencesKey("noti_index")
        private val KEY_NOTI_LAST_UPDATE = longPreferencesKey("noti_last_update_time")
    }
}