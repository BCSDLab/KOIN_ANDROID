package `in`.koreatech.koin.domain.repository

interface SignupRepository {
    suspend fun getPrivacyTermText() : String
    suspend fun getKoinTermText() : String

    suspend fun requestEmailVerification(
        portalAccount: String,
        gender: Int,
        isGraduated: Int,
        major: String,
        name:String,
        nickName: String,
        password: String,
        phoneNumber: String,
        studentNumber: String,
    ): Result<Unit>
}