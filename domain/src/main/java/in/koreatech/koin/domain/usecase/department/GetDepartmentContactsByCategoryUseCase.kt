package `in`.koreatech.koin.domain.usecase.department

import `in`.koreatech.koin.domain.model.department.DepartmentContactsByCategory
import `in`.koreatech.koin.domain.repository.DepartmentRepository
import javax.inject.Inject

class GetDepartmentContactsByCategoryUseCase @Inject constructor(
    private val departmentRepository: DepartmentRepository
) {
    suspend operator fun invoke(category: String, keyword: String? = null): Result<DepartmentContactsByCategory> {
        return departmentRepository.getDepartmentContactsByCategory(category, keyword)
    }
}
