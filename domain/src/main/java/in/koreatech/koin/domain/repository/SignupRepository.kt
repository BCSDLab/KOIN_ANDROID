package `in`.koreatech.koin.domain.repository

import `in`.koreatech.koin.domain.model.term.Term
import `in`.koreatech.koin.domain.model.user.Gender
import `in`.koreatech.koin.domain.model.user.Graduated

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

    suspend fun isUsernameDuplicatedV2(nickname: String): Result<Unit>

    suspend fun isPhoneDuplicated(phone: String): Result<Unit>

    suspend fun isLoginIdDuplicated(loginId: String): Result<Unit>

    suspend fun isEmailDuplicated(email: String): Result<Unit>

    suspend fun postStudentRegister(
        name: String,
        phoneNumber: String,
        loginId: String,
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
        loginId: String,
        password: String,
        gender: String,
        email: String,
        nickname: String
    ): Result<Unit>
}
