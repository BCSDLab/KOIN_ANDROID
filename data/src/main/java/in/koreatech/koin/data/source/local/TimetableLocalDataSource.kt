package `in`.koreatech.koin.data.source.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import dagger.hilt.android.qualifiers.ApplicationContext
import `in`.koreatech.koin.data.response.timetable.DepartmentResponse
import `in`.koreatech.koin.data.response.timetable.DepartmentsResponse
import `in`.koreatech.koin.data.util.readData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TimetableLocalDataSource @Inject constructor(
    @ApplicationContext private val context: Context,
    private val dataStore: DataStore<Preferences>
) {
    fun getString(key: String): Flow<String> =
        dataStore.data.map { preferences ->
            preferences[stringPreferencesKey(key)] ?: ""
        }

    suspend fun putString(key: String, value: String) {
        dataStore.edit { preferences ->
            preferences[stringPreferencesKey(key)] = value
        }
    }

    suspend fun putSemester(value: String) {
        dataStore.edit { preferences ->
            preferences[stringPreferencesKey("semester")] = value
        }
    }

    fun loadDepartments(): List<DepartmentResponse>? =
        context.readData<DepartmentsResponse>("department.json")?.departments
}