package `in`.koreatech.koin.data.repository

import `in`.koreatech.koin.data.source.local.DeptLocalDataSource
import `in`.koreatech.koin.data.source.remote.DeptRemoteDataSource
import `in`.koreatech.koin.domain.model.user.Dept
import `in`.koreatech.koin.domain.repository.DeptRepository
import javax.inject.Inject

class DeptRepositoryImpl @Inject constructor(
    private val deptRemoteDataSource: DeptRemoteDataSource,
    private val deptLocalDataSource: DeptLocalDataSource
) : DeptRepository{
    override suspend fun getDeptNameFromDeptCode(deptCode: String): String {
        return try {
            val deptResponse = deptRemoteDataSource.getDeptFromDeptCode(deptCode)
            deptResponse?.name ?: throw IllegalArgumentException()
        } catch (t: Throwable) {
            deptLocalDataSource.getDeptFromDeptCode(deptCode)
        }
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