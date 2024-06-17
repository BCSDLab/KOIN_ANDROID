package `in`.koreatech.koin.domain.repository

import `in`.koreatech.koin.domain.model.user.Dept

interface DeptRepository {
    suspend fun getDeptNameFromDeptCode(deptCode: String): String
    suspend fun getDepts(): List<Dept>
    suspend fun getDeptNames(): List<String>
}