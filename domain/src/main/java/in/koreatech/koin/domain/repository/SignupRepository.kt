package `in`.koreatech.koin.domain.repository

import `in`.koreatech.koin.domain.model.term.Term
import `in`.koreatech.koin.domain.model.user.CodeCount
import `in`.koreatech.koin.domain.model.user.Duplicated
import `in`.koreatech.koin.domain.model.user.Gender
import `in`.koreatech.koin.domain.model.user.Graduated
import `in`.koreatech.koin.domain.model.user.Verification

interface SignupRepository {
    suspend fun getPrivacyTermText(): String

    suspend fun getKoinTermText(): String

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

    suspend fun isUsernameDuplicatedV2(nickname: String): Duplicated

    suspend fun isPhoneDuplicated(phone: String): Duplicated

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
    ): Boolean

    suspend fun postGeneralRegister(
        name: String,
        phoneNumber: String,
        userId: String,
        password: String,
        gender: String,
        email: String,
        nickname: String
    ): Boolean

    suspend fun sendSMS(target: String): Boolean

    suspend fun verifyCertificationCode(target: String, code: String): Verification

    suspend fun countSMS(target: String): Result<CodeCount>
}
