package `in`.koreatech.koin.domain.usecase.user

import `in`.koreatech.koin.domain.constant.ERROR_INVALID_STUDENT_ID
import `in`.koreatech.koin.domain.constant.ERROR_USERINFO_GENDER_NOT_SET
import `in`.koreatech.koin.domain.constant.ERROR_USERINFO_NICKNAME_VALIDATION_NOT_CHECK
import `in`.koreatech.koin.domain.error.user.UserErrorHandler
import `in`.koreatech.koin.domain.model.error.ErrorHandler
import `in`.koreatech.koin.domain.model.user.Gender
import `in`.koreatech.koin.domain.model.user.User
import `in`.koreatech.koin.domain.repository.DeptRepository
import `in`.koreatech.koin.domain.repository.UserRepository
import `in`.koreatech.koin.domain.util.ext.isValidStudentId
import javax.inject.Inject

class UpdateUserInfoUseCase @Inject constructor(
    private val deptRepository: DeptRepository,
    private val userRepository: UserRepository,
    private val userErrorHandler: UserErrorHandler
) {
    suspend operator fun invoke(
        beforeUser: User,
        name: String,
        nickname: String,
        separatedPhoneNumber: List<String>?,
        gender: Gender?,
        studentId: String,
        checkedEmailValidation: Boolean
    ): ErrorHandler? {
        return try {
            if (!checkedEmailValidation) {
                throw IllegalStateException(ERROR_USERINFO_NICKNAME_VALIDATION_NOT_CHECK)
            }

            if (gender == null) {
                throw IllegalStateException(ERROR_USERINFO_GENDER_NOT_SET)
            }

            if(studentId.isNotEmpty()) {
                when {
                    !studentId.isValidStudentId -> {
                        throw IllegalStateException(ERROR_INVALID_STUDENT_ID)
                    }
                    else -> deptRepository.getDeptNameFromDeptCode(studentId.substring(5..6))
                }
            }

            val newUser = beforeUser.copy(
                name = name.trim(),
                nickname = nickname.trim(),
                phoneNumber = separatedPhoneNumber?.let {
                    it
                        .filter { it.isNotEmpty() }
                        .joinToString(separator = "-") { it.trim() }
                        .ifBlank { null }
                },
                gender = gender,
                studentNumber = studentId.trim().ifBlank { null }
            )

            userRepository.updateUser(newUser)
            null
        } catch (t: Throwable) {
            userErrorHandler.handleUpdateUserError(t)
        }
    }
}