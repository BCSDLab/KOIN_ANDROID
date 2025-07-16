package `in`.koreatech.koin.domain.usecase.user

import `in`.koreatech.koin.domain.model.user.Gender
import `in`.koreatech.koin.domain.model.user.User
import `in`.koreatech.koin.domain.repository.UserRepository
import javax.inject.Inject

class UpdateStudentUserInfoUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(
        beforeUser: User,
        email: String,
        name: String,
        nickname: String,
        gender: Gender,
        phoneNumber: String,
        studentNumber: String,
        major: String
    ): Result<Unit> {
        return runCatching {
            val user = (beforeUser as User.Student).copy(
                email = email,
                name = name,
                nickname = nickname,
                gender = gender,
                phoneNumber = phoneNumber,
                studentNumber = studentNumber,
                major = major
            )

            userRepository.updateUser(user)
        }
    }
}
