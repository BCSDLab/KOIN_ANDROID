package `in`.koreatech.koin.domain.repository

import `in`.koreatech.koin.domain.model.owner.Owner
import `in`.koreatech.koin.domain.model.user.AuthToken

interface OwnerRepository {
    suspend fun getToken(
        email: String,
        hashedPassword: String
    ): AuthToken

    suspend fun getOwnerInfo(): Owner

    suspend fun requestPasswordResetEmail(
        address: String
    )

    suspend fun deleteOwner()
    suspend fun isOwnerNameDuplicated(nickname: String): Boolean
    suspend fun updateOwner(owner: Owner)
}