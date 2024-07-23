package `in`.koreatech.koin.data.source.local

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = "koin_data_store"
)

class OnBoardingLocalDataSource @Inject constructor(
    @ApplicationContext context: Context
) {

    private val dataStore = context.dataStore

    private companion object {
        val KEY_SHOULD_SHOW_DINING_TOOLTIP = booleanPreferencesKey(
            "should_show_dining_tooltip"
        )
        val KEY_SHOULD_SHOW_NOTIFICATION_ON_BOARDING = booleanPreferencesKey(
            "should_show_notification_on_boarding"
        )
    }

    suspend fun updateShouldShowDiningTooltip(shouldShow: Boolean) {
        Result.runCatching {
            dataStore.edit { preferences ->
                preferences[KEY_SHOULD_SHOW_DINING_TOOLTIP] = shouldShow
            }
        }
    }

    suspend fun getShouldShowDiningTooltip(): Result<Boolean> {
        return Result.runCatching {
            val flow = dataStore.data.catch { e ->
                if (e is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw e
                }
            }.map { preferences ->
                preferences[KEY_SHOULD_SHOW_DINING_TOOLTIP] ?: true
            }
            flow.firstOrNull() ?: true
        }
    }

    suspend fun updateShouldShowNotificationOnBoarding(shouldShow: Boolean) {
        Result.runCatching {
            dataStore.edit { preferences ->
                preferences[KEY_SHOULD_SHOW_NOTIFICATION_ON_BOARDING] = shouldShow
            }
        }
    }

    suspend fun getShouldShowNotificationOnBoarding(): Result<Boolean> {
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