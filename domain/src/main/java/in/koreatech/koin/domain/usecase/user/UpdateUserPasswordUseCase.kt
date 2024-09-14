package `in`.koreatech.koin.domain.usecase.user

import `in`.koreatech.koin.domain.model.user.User
import `in`.koreatech.koin.domain.repository.UserRepository
import `in`.koreatech.koin.domain.util.ext.toSHA256
import javax.inject.Inject

class UpdateUserPasswordUseCase @Inject constructor(
    private val userRepository: UserRepository
) {

    suspend operator fun invoke(user: User, password: String): Result<Unit> {
        return runCatching {
            when (user) {
                is User.Anonymous -> {
                    throw throw IllegalAccessException("Updating anonymous user is not supported")
                }

                is User.Student -> {
                    userRepository.updateUserPassword(user, password.toSHA256())
                }
            }
        }
    }
}