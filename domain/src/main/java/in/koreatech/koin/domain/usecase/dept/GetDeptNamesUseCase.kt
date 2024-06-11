package `in`.koreatech.koin.domain.usecase.dept

import `in`.koreatech.koin.domain.repository.DeptRepository
import javax.inject.Inject

class GetDeptNamesUseCase @Inject constructor(
    private val deptRepository: DeptRepository,
) {
    suspend operator fun invoke(): List<String> {
        return deptRepository.getDeptNames()
    }
}