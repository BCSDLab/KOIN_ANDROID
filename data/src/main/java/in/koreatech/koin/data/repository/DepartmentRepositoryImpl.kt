package `in`.koreatech.koin.data.repository

import `in`.koreatech.koin.data.mapper.toDepartmentContacts
import `in`.koreatech.koin.data.mapper.toDepartmentContactsByCategory
import `in`.koreatech.koin.data.source.remote.DepartmentRemoteDataSource
import `in`.koreatech.koin.data.util.mapHttpFailure
import `in`.koreatech.koin.domain.error.department.KoinDepartmentException
import `in`.koreatech.koin.domain.model.department.DepartmentContacts
import `in`.koreatech.koin.domain.model.department.DepartmentContactsByCategory
import `in`.koreatech.koin.domain.repository.DepartmentRepository
import `in`.koreatech.koin.domain.util.suspendRunCatching
import javax.inject.Inject

class DepartmentRepositoryImpl @Inject constructor(
    private val departmentRemoteDataSource: DepartmentRemoteDataSource
) : DepartmentRepository {

    override suspend fun getDepartmentContacts(keyword: String?): Result<DepartmentContacts> {
        return suspendRunCatching {
            departmentRemoteDataSource.getDepartmentContacts(keyword).toDepartmentContacts()
        }
    }

    override suspend fun getDepartmentContactsByCategory(
        category: String,
        keyword: String?
    ): Result<DepartmentContactsByCategory> {
        return suspendRunCatching {
            departmentRemoteDataSource.getDepartmentContactsByCategory(category, keyword)
                .toDepartmentContactsByCategory()
        }.mapHttpFailure {
            on(404) throws KoinDepartmentException.DepartmentNotFoundException()
        }
    }
}
