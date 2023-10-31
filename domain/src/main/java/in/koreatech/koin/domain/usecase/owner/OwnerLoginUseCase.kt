package `in`.koreatech.koin.domain.usecase.owner

import `in`.koreatech.koin.domain.error.owner.OwnerErrorHandler
import `in`.koreatech.koin.domain.model.error.ErrorHandler
import `in`.koreatech.koin.domain.repository.OwnerRepository
import `in`.koreatech.koin.domain.repository.TokenRepository
import `in`.koreatech.koin.domain.util.ext.toSHA256
import javax.inject.Inject

class OwnerLoginUseCase @Inject constructor(
    private val ownerRepository: OwnerRepository,
    private val tokenRepository: TokenRepository,
    private val ownerErrorHandler: OwnerErrorHandler
) {
    suspend operator fun invoke(
        email: String,
        password: String
    ): Pair<Unit?, ErrorHandler?> {
        return try {
            val authToken = ownerRepository.getToken(email, password.toSHA256())
            tokenRepository.saveAccessToken(authToken.token)
            Unit to null
        } catch (throwable: Throwable) {
            null to ownerErrorHandler.handleGetTokenError(throwable)
        }
    }
}