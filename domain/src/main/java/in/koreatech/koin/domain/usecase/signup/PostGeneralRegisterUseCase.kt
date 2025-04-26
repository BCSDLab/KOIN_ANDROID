package `in`.koreatech.koin.domain.usecase.signup

import `in`.koreatech.koin.domain.repository.SignupRepository
import javax.inject.Inject

class PostGeneralRegisterUseCase @Inject constructor(
    private val signupRepository: SignupRepository
) {
    suspend operator fun invoke(
        name: String,
        phoneNumber: String,
        userId: String,
        password: String,
        gender: String,
        email: String,
        nickname: String
    ): Result<Unit> {
        return signupRepository.postGeneralRegister(
            name = name,
            phoneNumber = phoneNumber,
            userId = userId,
            password = password,
            gender = gender,
            email = email,
            nickname = nickname
        )
    }
}
