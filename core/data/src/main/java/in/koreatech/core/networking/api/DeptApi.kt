package `in`.koreatech.koin.data.api

import `in`.koreatech.koin.data.constant.URLConstant
import `in`.koreatech.core.networking.response.dept.DeptResponse
import `in`.koreatech.core.networking.response.dept.DetailDeptResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface DeptApi {
    @GET(URLConstant.DEPT.DEPT)
    suspend fun getDept(@Query("dept_num") deptNum: String) : DeptResponse?

    @GET(URLConstant.DEPT.DEPTS)
    suspend fun getDepts() : List<DetailDeptResponse>
}