package `in`.koreatech.koin.data.source.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class OnboardingLocalDataSource @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {

    suspend fun getShouldOnboarding(onboardingType: String): Boolean {
        return dataStore.data.catch {
            emit(emptyPreferences())
        }.map { preferences ->
            preferences[booleanPreferencesKey(onboardingType)] ?: true
        }.firstOrNull() ?: true
    }

    suspend fun updateShouldOnboarding(onboardingType: String, shouldShow: Boolean) {
        dataStore.edit { preferences ->
            preferences[booleanPreferencesKey(onboardingType)] = shouldShow
        }
    }
}