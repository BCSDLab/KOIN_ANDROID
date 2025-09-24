package `in`.koreatech.koin.data.source.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.google.gson.Gson
import java.time.Instant
import javax.inject.Inject
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import timber.log.Timber

class SessionDataStore @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    private val gson = Gson()

    suspend fun getSessionId(
        sessionName: String,
        isLoggedIn: Boolean,
        platform: String,
        sessionTime: Int,
        shouldExpireOtherSessions: Boolean
    ): String {
        val sessionsJson = dataStore.data.map { preferences ->
            preferences[KEY_LOGGING_SESSION]
        }.firstOrNull()

        val sessionId = if (sessionsJson.isNullOrEmpty()) null else gson.fromJson<List<String>>(sessionsJson, List::class.java).firstOrNull { it.startsWith(sessionName) }

        if (sessionId == null || sessionId.isSessionIdExpired(sessionTime)) {
            if (sessionId != null) {
                removeSessionId(sessionId)
            }

            val newSessionId = "${sessionName}_${if (isLoggedIn) 1 else 0}_${platform.uppercase()}_${Instant.now().epochSecond}_${generateRandomSessionString()}"
            Timber.d(newSessionId) // print new session id
            updateSessionId(newSessionId, shouldExpireOtherSessions)
            return newSessionId
        }
        return sessionId
    }

    suspend fun updateSessionId(
        sessionId: String,
        shouldExpireOtherSessions: Boolean
    ) {
        dataStore.edit { preferences ->
            val sessionsJson = preferences[KEY_LOGGING_SESSION]
            val list = if (shouldExpireOtherSessions || sessionsJson.isNullOrEmpty()) {
                listOf(sessionId)
            } else {
                gson.fromJson<List<String>>(sessionsJson, List::class.java).toMutableList().apply {
                    add(sessionId)
                }
            }
            preferences[KEY_LOGGING_SESSION] = gson.toJson(list)
        }
    }

    suspend fun removeSessionId(
        sessionId: String
    ) {
        dataStore.edit { preferences ->
            val sessionsJson = preferences[KEY_LOGGING_SESSION]
            val list = gson.fromJson<List<String>>(sessionsJson, List::class.java).toMutableList().apply {
                remove(sessionId)
            }
            preferences[KEY_LOGGING_SESSION] = gson.toJson(list)
        }
    }

    private fun String.isSessionIdExpired(sessionTime: Int): Boolean {
        return Instant.now().epochSecond - split("_").let { it[it.size - 2] }.toInt() >= sessionTime
    }

    private fun generateRandomSessionString(): String {
        return (0 until 5).map {
            ('A'..'Z').random()
        }.joinToString("")
    }

    companion object {
        private val KEY_LOGGING_SESSION = stringPreferencesKey("logging_session")
    }
}
