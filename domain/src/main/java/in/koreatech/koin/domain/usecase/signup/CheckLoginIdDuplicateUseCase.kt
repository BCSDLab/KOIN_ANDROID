package `in`.koreatech.koin.domain.usecase.signup

import `in`.koreatech.koin.domain.repository.SignupRepository
import `in`.koreatech.koin.domain.state.signup.SignupContinuationState
import javax.inject.Inject

class CheckLoginIdDuplicateUseCase @Inject constructor(
    private val signupRepository: SignupRepository
) {
    suspend operator fun invoke(userId: String): SignupContinuationState {
        return signupRepository.isLoginIdDuplicated(userId)
    }
}
