package `in`.koreatech.koin.data.source.remote

import `in`.koreatech.koin.data.api.DeptApi
import javax.inject.Inject

class DeptRemoteDataSource @Inject constructor(
    private val deptApi: DeptApi
) {
    suspend fun getAllDepts() = deptApi.getDepts()
    suspend fun getDeptFromDeptCode(deptCode: String) = deptApi.getDept(deptCode)
}