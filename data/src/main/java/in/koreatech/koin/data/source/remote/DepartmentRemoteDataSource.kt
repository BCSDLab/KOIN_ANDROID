package `in`.koreatech.koin.data.source.remote

import `in`.koreatech.koin.data.api.DepartmentApi
import `in`.koreatech.koin.data.response.department.DepartmentContactsByCategoryResponse
import `in`.koreatech.koin.data.response.department.DepartmentContactsResponse
import javax.inject.Inject

class DepartmentRemoteDataSource @Inject constructor(
    private val departmentApi: DepartmentApi
) {
    suspend fun getDepartmentContacts(keyword: String?): DepartmentContactsResponse =
        departmentApi.getDepartmentContacts(keyword)

    suspend fun getDepartmentContactsByCategory(category: String, keyword: String?): DepartmentContactsByCategoryResponse =
        departmentApi.getDepartmentContactsByCategory(category, keyword)
}
