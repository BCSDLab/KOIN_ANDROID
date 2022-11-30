package `in`.koreatech.koin.domain.usecase.dept

import `in`.koreatech.koin.domain.error.dept.DeptErrorHandler
import `in`.koreatech.koin.domain.model.error.ErrorHandler
import `in`.koreatech.koin.domain.repository.DeptRepository
import `in`.koreatech.koin.domain.repository.UserRepository
import `in`.koreatech.koin.domain.util.ext.isValidStudentId
import javax.inject.Inject

class GetDeptNameFromStudentIdUseCase @Inject constructor(
    private val deptRepository: DeptRepository,
    private val deptErrorHandler: DeptErrorHandler
){
    suspend operator fun invoke(studentId: String) : Pair<String?, ErrorHandler?> {

        if(!studentId.isValidStudentId) return "" to null

        return try {
            deptRepository.getDeptNameFromDeptCode(studentId.substring(5..6)) to null
        } catch (t: Throwable) {
            null to deptErrorHandler.getDeptNameFromDeptCodeError(t)
        }
    }
}