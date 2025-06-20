package `in`.koreatech.koin.domain.usecase.signup

import `in`.koreatech.koin.domain.repository.SignupRepository
import javax.inject.Inject

class CheckNicknameDuplicateUseCase @Inject constructor(
    private val signupRepository: SignupRepository
) {
    suspend operator fun invoke(username: String): Result<Unit> {
        return signupRepository.isUsernameDuplicatedV2(username)
    }
}
