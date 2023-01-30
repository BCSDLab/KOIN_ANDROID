package `in`.koreatech.koin.domain.usecase.dept

import `in`.koreatech.koin.domain.error.dept.DeptErrorHandler
import `in`.koreatech.koin.domain.model.error.ErrorHandler
import `in`.koreatech.koin.domain.repository.DeptRepository
import `in`.koreatech.koin.domain.repository.UserRepository
import `in`.koreatech.koin.domain.util.ext.isValidStudentId
import javax.inject.Inject
import `in`.koreatech.koin.domain.model.Result
import `in`.koreatech.koin.domain.model.toResult

class GetDeptNameFromStudentIdUseCase @Inject constructor(
    private val deptRepository: DeptRepository,
    private val deptErrorHandler: DeptErrorHandler
){
    suspend operator fun invoke(studentId: String) : Result<String> {

        if(studentId.length < 10) return "".toResult()

        return try {
            deptRepository.getDeptNameFromDeptCode(studentId.substring(5..6)).toResult()
        } catch (t: Throwable) {
            deptErrorHandler.getDeptNameFromDeptCodeError(t)
        }
    }
}