package `in`.koreatech.koin.data.repository

import `in`.koreatech.koin.data.request.user.LoginRequest
import `in`.koreatech.koin.data.source.local.SignupTermsLocalDataSource
import `in`.koreatech.koin.data.source.remote.UserRemoteDataSource
import `in`.koreatech.koin.domain.repository.SignupRepository
import javax.inject.Inject

class SignupRepositoryImpl @Inject constructor(
    private val userRemoteDataSource: UserRemoteDataSource,
    private val signupTermsLocalDataSource: SignupTermsLocalDataSource
) : SignupRepository {
    override suspend fun getPrivacyTermText(): String {
        return signupTermsLocalDataSource.getPrivacyTermText()
    }

    override suspend fun getKoinTermText(): String {
        return signupTermsLocalDataSource.getKoinTermText()
    }

    override suspend fun requestEmailVerification(portalAccount: String, hashedPassword: String) {
        userRemoteDataSource.sendRegisterEmail(
            LoginRequest(
                portalAccount = portalAccount,
                passwordHashed = hashedPassword
            )
        )
    }
}