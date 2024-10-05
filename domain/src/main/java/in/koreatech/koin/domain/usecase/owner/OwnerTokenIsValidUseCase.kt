package `in`.koreatech.koin.domain.usecase.owner

import `in`.koreatech.koin.domain.repository.OwnerSignupRepository
import `in`.koreatech.koin.domain.repository.UserRepository
import `in`.koreatech.koin.domain.state.signup.SignupContinuationState
import `in`.koreatech.koin.domain.util.ext.isNotValidEmail
import `in`.koreatech.koin.domain.util.ext.isNotValidPassword
import javax.inject.Inject

class OwnerTokenIsValidUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    operator fun invoke(): Boolean{
        return userRepository.ownerTokenIsValid()
    }
}