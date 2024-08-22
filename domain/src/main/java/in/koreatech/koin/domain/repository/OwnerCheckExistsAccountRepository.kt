package `in`.koreatech.koin.domain.repository

interface OwnerCheckExistsAccountRepository {
    suspend fun checkExistsAccount(
        phoneNumber: String
    ): Boolean
}