package `in`.koreatech.koin.domain.usecase.user

import `in`.koreatech.koin.domain.constant.ERROR_INVALID_PHONE_NUMBER
import `in`.koreatech.koin.domain.constant.ERROR_INVALID_STUDENT_ID
import `in`.koreatech.koin.domain.constant.ERROR_USERINFO_NICKNAME_VALIDATION_NOT_CHECK
import `in`.koreatech.koin.domain.error.user.UserErrorHandler
import `in`.koreatech.koin.domain.model.error.ErrorHandler
import `in`.koreatech.koin.domain.model.user.Gender
import `in`.koreatech.koin.domain.model.user.User
import `in`.koreatech.koin.domain.repository.UserRepository
import `in`.koreatech.koin.domain.util.ext.formatPhoneNumber
import `in`.koreatech.koin.domain.util.ext.isValidStudentId
import javax.inject.Inject

class UpdateStudentUserInfoUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val userErrorHandler: UserErrorHandler
) {
    suspend operator fun invoke(
        beforeUser: User,
        name: String?,
        nickname: String?,
        rawPhoneNumber: String?,
        gender: Gender,
        studentId: String?,
        major: String?,
        isCheckNickname: Boolean
    ): ErrorHandler? {
        return try {
            if (!nickname.isNullOrBlank() && !isCheckNickname) {
                throw IllegalStateException(ERROR_USERINFO_NICKNAME_VALIDATION_NOT_CHECK)
            }

            if (!studentId.isNullOrBlank() && !studentId.isValidStudentId) {
                throw IllegalStateException(ERROR_INVALID_STUDENT_ID)
            }

            val phoneNumber = rawPhoneNumber?.trim()?.formatPhoneNumber()

            if (!phoneNumber.isNullOrBlank() && phoneNumber.length != 13) {
                throw IllegalStateException(ERROR_INVALID_PHONE_NUMBER)
            }

            val newUser: User = when (beforeUser) {
                User.Anonymous -> throw IllegalAccessException()
                is User.Student -> {
                    beforeUser.copy(
                        name = name?.trim()?.ifBlank { null },
                        nickname = nickname?.trim()?.ifBlank { null },
                        phoneNumber = phoneNumber?.ifBlank { null },
                        gender = gender,
                        studentNumber = studentId?.trim()?.ifBlank { null },
                        major = major?.ifBlank { null }
                    )
                }
            }

            userRepository.updateUser(newUser)
            null
        } catch (t: Throwable) {
            userErrorHandler.handleUpdateUserError(t)
        }
    }
}