package `in`.koreatech.koin.data.repository

import `in`.koreatech.koin.data.source.remote.DeptRemoteDataSource
import `in`.koreatech.koin.domain.model.user.Dept
import `in`.koreatech.koin.domain.repository.DeptRepository
import javax.inject.Inject

class DeptRepositoryImpl @Inject constructor(
    private val deptRemoteDataSource: DeptRemoteDataSource
) : DeptRepository{
    override suspend fun getDeptNameFromDeptCode(deptCode: String): String {
        val deptResponse = deptRemoteDataSource.getDeptFromDeptCode(deptCode)
        return deptResponse?.name ?: throw IllegalArgumentException()
    }

    override suspend fun getDepts(): List<Dept> {
        return deptRemoteDataSource.getAllDepts().map {
            Dept(
                name = it.name,
                curriculumUrl = it.curriculumLinkUrl,
                codes = it.deptNums
            )
        }
    }
}