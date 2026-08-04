package `in`.koreatech.koin.domain.usecase.department

import `in`.koreatech.koin.domain.model.department.DepartmentContacts
import `in`.koreatech.koin.domain.repository.DepartmentRepository
import javax.inject.Inject

class GetDepartmentContactsUseCase @Inject constructor(
    private val departmentRepository: DepartmentRepository
) {
    suspend operator fun invoke(keyword: String? = null): Result<DepartmentContacts> {
        return departmentRepository.getDepartmentContacts(keyword)
    }
}
