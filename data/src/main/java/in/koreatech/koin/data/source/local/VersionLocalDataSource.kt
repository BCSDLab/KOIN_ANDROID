package `in`.koreatech.koin.data.source.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

class VersionLocalDataSource @Inject constructor(
    @ApplicationContext private val applicationContext: Context
) {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = PREF_NAME)

    private val dataStore = applicationContext.dataStore

    suspend fun updateLatestVersionCode(versionCode: Int) {
        try {
            dataStore.edit { pref ->
                pref[KEY_LATEST_VERSION_CODE] = versionCode
            }
        } catch (e: Exception) {
            if (e !is IOException) throw e
        }
    }

    suspend fun updateLatestVersionName(versionName: String) {
        try {
            dataStore.edit { pref ->
                pref[KEY_LATEST_VERSION_NAME] = versionName
            }
        } catch (e: Exception) {
            if (e !is IOException) throw e
        }
    }

    suspend fun getLatestVersionCode(): Int? {
        return dataStore.data
            .catch { if (it is IOException) emit(emptyPreferences()) else throw it }
            .map { pref ->
                pref[KEY_LATEST_VERSION_CODE]
            }.firstOrNull()
    }

    suspend fun getLatestVersionName(): String? {
        return dataStore.data
            .catch { if (it is IOException) emit(emptyPreferences()) else throw it }
            .map { pref ->
                pref[KEY_LATEST_VERSION_NAME]
            }.firstOrNull()
    }

    // 이후 버전 코드가 2^31 이상이 되는 경우 개선
    @Suppress("DEPRECATION")
    fun getCurrentVersionCode(): Int? = try {
        applicationContext.packageManager.getPackageInfo(
            applicationContext.packageName, 0
        ).versionCode
    } catch (e: Exception) {
        null
    }

    fun getCurrentVersionName(): String? = try {
        applicationContext.packageManager.getPackageInfo(
            applicationContext.packageName, 0
        ).versionName
    } catch (e: Exception) {
        null
    }

    private companion object {
        const val PREF_NAME = "PREF_VERSION"
        val KEY_LATEST_VERSION_CODE = intPreferencesKey("KEY_USER_LATEST_VERSION_CODE")
        val KEY_LATEST_VERSION_NAME = stringPreferencesKey("KEY_USER_LATEST_VERSION_NAME")
    }
}