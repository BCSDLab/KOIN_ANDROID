package `in`.koreatech.koin.domain.repository

import `in`.koreatech.koin.domain.model.club.ClubCategories

interface ClubRepository {
    suspend fun getClubsCategories(): ClubCategories
}
