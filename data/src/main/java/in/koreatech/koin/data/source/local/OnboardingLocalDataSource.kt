package `in`.koreatech.koin.data.source.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = "koin_data_store"
)

class OnboardingLocalDataSource @Inject constructor(
    context: Context
) {

    private val dataStore = context.dataStore

    suspend fun getShouldShowTooltip(onboardingType: String): Boolean {
        return dataStore.data.catch {
            emit(emptyPreferences())
        }.map { preferences ->
            preferences[booleanPreferencesKey(onboardingType)] ?: true
        }.firstOrNull() ?: true
    }

    suspend fun updateShouldShowTooltip(onboardingType: String, shouldShow: Boolean) {
        dataStore.edit { preferences ->
            preferences[booleanPreferencesKey(onboardingType)] = shouldShow
        }
    }

    private companion object {
        val KEY_SHOULD_SHOW_NOTIFICATION_ON_BOARDING = booleanPreferencesKey(
            "should_show_notification_on_boarding"
        )
    }

    suspend fun updateShouldShowNotificationOnboarding(shouldShow: Boolean) {
        Result.runCatching {
            dataStore.edit { preferences ->
                preferences[KEY_SHOULD_SHOW_NOTIFICATION_ON_BOARDING] = shouldShow
            }
        }
    }

    suspend fun getShouldShowNotificationOnboarding(): Result<Boolean> {
        return Result.runCatching {
            val flow = dataStore.data.catch { e ->
                if (e is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw e
                }
            }.map { preferences ->
                preferences[KEY_SHOULD_SHOW_NOTIFICATION_ON_BOARDING] ?: true
            }
            flow.firstOrNull() ?: true
        }
    }
}