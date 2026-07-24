package `in`.koreatech.koin.data.api

import `in`.koreatech.koin.data.response.department.DepartmentContactsByCategoryResponse
import `in`.koreatech.koin.data.response.department.DepartmentContactsResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface DepartmentApi {
    @GET("department-contacts")
    suspend fun getDepartmentContacts(
        @Query("keyword") keyword: String? = null
    ): DepartmentContactsResponse

    @GET("department-contacts/{category}")
    suspend fun getDepartmentContactsByCategory(
        @Path("category") category: String,
        @Query("keyword") keyword: String? = null
    ): DepartmentContactsByCategoryResponse
}
