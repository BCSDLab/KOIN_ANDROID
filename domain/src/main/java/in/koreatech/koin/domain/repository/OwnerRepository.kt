package `in`.koreatech.koin.domain.repository

interface OwnerRepository {
    suspend fun getOwnerName(): String
}