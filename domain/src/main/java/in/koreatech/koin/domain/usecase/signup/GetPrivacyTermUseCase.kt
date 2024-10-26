package `in`.koreatech.koin.domain.usecase.signup

import `in`.koreatech.koin.domain.model.term.Term
import `in`.koreatech.koin.domain.repository.SignupRepository
import javax.inject.Inject

class GetPrivacyTermUseCase @Inject constructor(
    private val signupRepository: SignupRepository
) {
    suspend operator fun invoke(): Result<Term> = kotlin.runCatching {
        signupRepository.getPrivacyTerm()
    }
}