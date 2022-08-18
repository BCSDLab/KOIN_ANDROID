package `in`.koreatech.koin.domain.usecase.user

import `in`.koreatech.koin.domain.model.user.User
import `in`.koreatech.koin.domain.repository.UserRepository
import javax.inject.Inject

class GetUserInfoUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke() : Result<User> {
        return kotlin.runCatching { userRepository.getUserInfo() }
    }
}