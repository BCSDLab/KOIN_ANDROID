package `in`.koreatech.koin.domain.repository

import `in`.koreatech.koin.domain.model.user.Dept

class FakeDeptRepository : DeptRepository {
    private var deptCodeToName: Map<String, String> = emptyMap()
    private var fakeDepts: List<Dept> = emptyList()
    private var shouldThrow: Boolean = false

    fun setDeptNameForCode(code: String, name: String) {
        deptCodeToName = deptCodeToName + (code to name)
    }

    fun setFakeDepts(depts: List<Dept>) {
        fakeDepts = depts
    }

    fun setShouldThrow(shouldThrow: Boolean) {
        this.shouldThrow = shouldThrow
    }

    override suspend fun getDeptNameFromDeptCode(deptCode: String): String {
        if (shouldThrow) throw RuntimeException("Network error")
        return deptCodeToName[deptCode] ?: throw NoSuchElementException("No dept for code: $deptCode")
    }

    override suspend fun getDepts(): List<Dept> {
        if (shouldThrow) throw RuntimeException("Network error")
        return fakeDepts
    }

    override suspend fun getDeptNames(): List<String> {
        if (shouldThrow) throw RuntimeException("Network error")
        return fakeDepts.map { it.name }
    }
}
