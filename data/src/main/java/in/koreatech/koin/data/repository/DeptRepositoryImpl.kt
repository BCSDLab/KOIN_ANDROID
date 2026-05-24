package `in`.koreatech.koin.data.repository

import `in`.koreatech.koin.data.source.local.DeptLocalDataSource
import `in`.koreatech.koin.data.source.remote.DeptRemoteDataSource
import `in`.koreatech.koin.data.util.mapHttpFailure
import `in`.koreatech.koin.domain.error.dept.KoinDeptException
import `in`.koreatech.koin.domain.model.user.Dept
import `in`.koreatech.koin.domain.repository.DeptRepository
import javax.inject.Inject
import kotlinx.coroutines.CancellationException

class DeptRepositoryImpl @Inject constructor(
    private val deptRemoteDataSource: DeptRemoteDataSource,
    private val deptLocalDataSource: DeptLocalDataSource
) : DeptRepository {
    @Suppress("TooGenericExceptionCaught", "InstanceOfCheckForException")
    override suspend fun getDeptNameFromDeptCode(deptCode: String): Result<String> {
        var remoteException: Throwable? = null
        // Step 1: Try remote fetch
        val deptResponse = try {
            deptRemoteDataSource.getDeptFromDeptCode(deptCode)
        } catch (t: Throwable) {
            if (t is CancellationException) throw t
            remoteException = t
            null
        }

        // Step 2: Check remote response
        if (deptResponse != null && deptResponse.name != null) {
            // Remote success with non-null name
            return Result.success(deptResponse.name)
        }

        // Step 3: Remote success but name == null, OR remote failed; try local fallback
        return try {
            Result.success(deptLocalDataSource.getDeptFromDeptCode(deptCode))
        } catch (localT: Throwable) {
            if (localT is CancellationException) throw localT
            // If we had a remote exception, map it; otherwise, use local exception
            if (remoteException != null) {
                Result.failure<String>(remoteException).mapHttpFailure {
                    on(404) throws KoinDeptException.DeptNotFoundException()
                }
            } else {
                // Remote succeeded but name was null; local failed; surface local exception
                Result.failure(localT)
            }
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

    override suspend fun getDeptNames(): List<String> {
        return try {
            deptRemoteDataSource.getAllDepts().map {
                it.name
            }
        } catch (t: Throwable) {
            deptLocalDataSource.getDeptNames()
        }
    }
}
