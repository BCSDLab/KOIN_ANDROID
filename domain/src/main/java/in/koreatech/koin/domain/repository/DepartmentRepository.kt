package `in`.koreatech.koin.domain.repository

import `in`.koreatech.koin.domain.model.department.DepartmentContacts
import `in`.koreatech.koin.domain.model.department.DepartmentContactsByCategory

interface DepartmentRepository {
    suspend fun getDepartmentContacts(keyword: String? = null): Result<DepartmentContacts>

    suspend fun getDepartmentContactsByCategory(category: String, keyword: String? = null): Result<DepartmentContactsByCategory>
}
