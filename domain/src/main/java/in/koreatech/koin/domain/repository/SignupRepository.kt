package `in`.koreatech.koin.domain.repository

import `in`.koreatech.koin.domain.model.user.Gender
import `in`.koreatech.koin.domain.model.user.Graduated

interface SignupRepository {
    suspend fun getPrivacyTermText() : String
    suspend fun getKoinTermText() : String

    suspend fun requestEmailVerification(
            portalAccount: String,
            gender: Gender,
            isGraduated: Graduated,
            major: String,
            name:String,
            nickName: String,
            password: String,
            phoneNumber: String,
            studentNumber: String,
    ): Result<Unit>
}