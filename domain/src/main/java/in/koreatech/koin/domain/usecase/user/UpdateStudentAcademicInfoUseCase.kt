package `in`.koreatech.koin.domain.usecase.user

import `in`.koreatech.koin.domain.model.user.StudentAcademicInfo
import `in`.koreatech.koin.domain.repository.UserRepository
import javax.inject.Inject

class UpdateStudentAcademicInfoUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(
        department: String,
        major: String?,
        studentNumber: String?
    ): Result<StudentAcademicInfo> {
        return userRepository.updateStudentAcademicInfo(department, major, studentNumber)
    }
}
