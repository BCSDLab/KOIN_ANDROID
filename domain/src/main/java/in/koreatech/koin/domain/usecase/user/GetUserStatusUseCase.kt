package `in`.koreatech.koin.domain.usecase.user

import `in`.koreatech.koin.domain.model.user.User
import `in`.koreatech.koin.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUserStatusUseCase @Inject constructor(
   private val userRepository: UserRepository
) {
    operator fun invoke(): Flow<User> =
        userRepository.getUserInfoFlow()
}