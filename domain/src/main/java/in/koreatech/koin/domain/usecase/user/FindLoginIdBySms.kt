package `in`.koreatech.koin.domain.usecase.user

import `in`.koreatech.koin.domain.repository.UserRepository
import javax.inject.Inject

class FindLoginIdBySms @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(phone: String, verificationCode: String): Result<String> {
        return userRepository.findLoginIdBySms(phone, verificationCode)
    }
}
