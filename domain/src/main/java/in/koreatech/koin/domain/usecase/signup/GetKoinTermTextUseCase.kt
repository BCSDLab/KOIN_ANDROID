package `in`.koreatech.koin.domain.usecase.signup

import `in`.koreatech.koin.domain.repository.SignupRepository
import javax.inject.Inject

class GetKoinTermTextUseCase @Inject constructor(
    private val signupRepository: SignupRepository
) {
    suspend operator fun invoke() : Result<String> {
        return kotlin.runCatching {
            signupRepository.getKoinTermText()
        }
    }
}