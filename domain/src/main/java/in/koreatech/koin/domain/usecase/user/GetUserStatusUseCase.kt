package `in`.koreatech.koin.domain.usecase.user

import `in`.koreatech.koin.domain.model.user.User
import `in`.koreatech.koin.domain.repository.UserRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetUserStatusUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    operator fun invoke(): Flow<User> = userRepository.getUserInfoFlow()
}
