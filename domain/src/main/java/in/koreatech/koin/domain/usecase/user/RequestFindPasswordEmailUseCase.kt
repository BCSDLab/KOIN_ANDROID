package `in`.koreatech.koin.domain.usecase.user

import `in`.koreatech.koin.domain.repository.UserRepository
import javax.inject.Inject

class RequestFindPasswordEmailUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(portalAccount: String) : Result<Unit> {
        if(portalAccount.isBlank()) return Result.failure(IllegalArgumentException("KOREATECH 이메일을 입력해주세요."))
        return kotlin.runCatching {
            userRepository.requestPasswordResetEmail(portalAccount)
        }
    }
}