package `in`.koreatech.koin.domain.usecase.user

import `in`.koreatech.koin.domain.model.user.Gender
import `in`.koreatech.koin.domain.model.user.User
import `in`.koreatech.koin.domain.repository.UserRepository
import javax.inject.Inject

class UpdateGeneralUserInfoUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(
        beforeUser: User,
        email: String,
        name: String,
        nickname: String,
        gender: Gender,
        phoneNumber: String
    ): Result<Unit> {
        return runCatching {
            val user = (beforeUser as User.General).copy(
                email = email.ifEmpty { null },
                name = name,
                nickname = nickname.ifEmpty { null },
                gender = gender,
                phoneNumber = phoneNumber
            )

            userRepository.updateUser(user)
        }
    }
}
