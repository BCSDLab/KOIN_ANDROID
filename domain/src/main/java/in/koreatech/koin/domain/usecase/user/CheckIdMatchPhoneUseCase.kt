package `in`.koreatech.koin.domain.usecase.user

import `in`.koreatech.koin.domain.repository.UserRepository
import javax.inject.Inject

class CheckIdMatchPhoneUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(loginId: String, phone: String): Result<Unit> {
        return runCatching {
            userRepository.checkIdMatchPhone(loginId, phone)
        }
    }
}