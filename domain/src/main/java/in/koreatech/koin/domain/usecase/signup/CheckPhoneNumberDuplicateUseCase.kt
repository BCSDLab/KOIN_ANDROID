package `in`.koreatech.koin.domain.usecase.signup

import `in`.koreatech.koin.domain.repository.SignupRepository
import javax.inject.Inject

class CheckPhoneNumberDuplicateUseCase @Inject constructor(
    private val signupRepository: SignupRepository
) {
    suspend operator fun invoke(phoneNumber: String): Result<Unit> {
        return signupRepository.isPhoneDuplicated(phoneNumber)
    }
}
