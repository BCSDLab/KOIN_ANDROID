package `in`.koreatech.koin.domain.repository

import `in`.koreatech.koin.domain.model.user.Dept
import kotlin.Result

interface DeptRepository {
    suspend fun getDeptNameFromDeptCode(deptCode: String): Result<String>

    suspend fun getDepts(): List<Dept>

    suspend fun getDeptNames(): List<String>
}
