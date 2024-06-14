package `in`.koreatech.koin.data.source.local

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import `in`.koreatech.koin.data.response.timetable.DepartmentResponse
import `in`.koreatech.koin.data.response.timetable.DepartmentsResponse
import `in`.koreatech.koin.data.util.readData
import javax.inject.Inject

class TimetableLocalDataSource @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    fun loadDepartments(): List<DepartmentResponse>? =
        context.readData<DepartmentsResponse>("department.json")?.departments
}