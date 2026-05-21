package `in`.koreatech.koin.domain.usecase.dept

import `in`.koreatech.koin.domain.repository.DeptRepository
import `in`.koreatech.koin.domain.util.deptCode
import `in`.koreatech.koin.domain.util.ext.isValidStudentId
import javax.inject.Inject
import kotlin.Result

class GetDeptNameFromStudentIdUseCase @Inject constructor(
    private val deptRepository: DeptRepository
) {
    suspend operator fun invoke(studentId: String): Result<String> {
        if (!studentId.isValidStudentId) return Result.success("")

        return deptRepository.getDeptNameFromDeptCode(studentId.deptCode)
    }
}
