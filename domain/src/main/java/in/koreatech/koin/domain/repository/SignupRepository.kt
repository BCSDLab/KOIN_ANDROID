package `in`.koreatech.koin.domain.repository

import `in`.koreatech.koin.domain.model.term.Term
import `in`.koreatech.koin.domain.model.user.Gender
import `in`.koreatech.koin.domain.model.user.Graduated
import `in`.koreatech.koin.domain.state.signup.SignupContinuationState

interface SignupRepository {
    suspend fun getPrivacyTermText(): String

    suspend fun getKoinTermText(): String

    suspend fun getMarketingTermText(): String

    suspend fun requestEmailVerification(
        portalAccount: String,
        gender: Gender,
        isGraduated: Graduated?,
        major: String?,
        name: String?,
        nickName: String?,
        password: String,
        phoneNumber: String?,
        studentNumber: String?
    ): Result<Unit>

    suspend fun getPrivacyTerm(): Term

    suspend fun getKoinTerm(): Term

    suspend fun getMarketingTerm(): Term

    suspend fun isUsernameDuplicatedV2(nickname: String): SignupContinuationState

    suspend fun isPhoneDuplicated(phone: String): SignupContinuationState

    suspend fun isUserIdDuplicated(userId: String): SignupContinuationState

    suspend fun postStudentRegister(
        name: String,
        phoneNumber: String,
        userId: String,
        password: String,
        department: String,
        studentNumber: String,
        gender: String,
        email: String,
        nickname: String
    ): Result<Unit>

    suspend fun postGeneralRegister(
        name: String,
        phoneNumber: String,
        userId: String,
        password: String,
        gender: String,
        email: String,
        nickname: String
    ): Result<Unit>

    suspend fun requestSmsVerification(phoneNumber: String): SignupContinuationState

    suspend fun verifyCertificationCode(phoneNumber: String, verificationCode: String): SignupContinuationState
}
