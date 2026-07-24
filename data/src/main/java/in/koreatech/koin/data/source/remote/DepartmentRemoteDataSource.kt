package `in`.koreatech.koin.data.source.remote

import `in`.koreatech.koin.data.api.DepartmentApi
import javax.inject.Inject

class DepartmentRemoteDataSource @Inject constructor(
    private val departmentApi: DepartmentApi
) {
    suspend fun getDepartmentContacts(keyword: String?) =
        departmentApi.getDepartmentContacts(keyword)

    suspend fun getDepartmentContactsByCategory(category: String, keyword: String?) =
        departmentApi.getDepartmentContactsByCategory(category, keyword)
}
