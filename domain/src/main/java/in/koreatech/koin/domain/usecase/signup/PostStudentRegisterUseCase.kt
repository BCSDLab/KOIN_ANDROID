package `in`.koreatech.koin.domain.usecase.signup

import `in`.koreatech.koin.domain.repository.SignupRepository
import javax.inject.Inject

class PostStudentRegisterUseCase @Inject constructor(
    private val signupRepository: SignupRepository
) {
    suspend operator fun invoke(
        name: String,
        phoneNumber: String,
        userId: String,
        password: String,
        department: String,
        studentNumber: String,
        gender: String,
        email: String,
        nickname: String
    ): Result<Unit> {
        return signupRepository.postStudentRegister(
            name = name,
            phoneNumber = phoneNumber,
            userId = userId,
            password = password,
            department = department,
            studentNumber = studentNumber,
            gender = gender,
            email = email,
            nickname = nickname
        )
    }
}
