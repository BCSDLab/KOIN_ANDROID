package `in`.koreatech.koin.domain.repository

import `in`.koreatech.koin.domain.model.club.ClubCategories
import `in`.koreatech.koin.domain.model.club.ClubDetails
import `in`.koreatech.koin.domain.model.club.ClubHot
import `in`.koreatech.koin.domain.model.club.ClubQnasInfo

interface ClubRepository {
    suspend fun getClubsCategories(): Result<ClubCategories>

    suspend fun getClubHot(): Result<ClubHot>

    suspend fun getClubDetails(
        clubId: Int
    ): Result<ClubDetails>

    suspend fun getClubQnas(
        clubId: Int
    ): Result<ClubQnasInfo>

    suspend fun setClubEmpowerment(
        clubId: Int,
        changedManagerId: String
    ): Result<Unit>

    suspend fun setClubLike(
        clubId: Int
    ): Result<Unit>

    suspend fun postClubQna(
        clubId: Int,
        parentId: Int?,
        content: String
    ): Result<Unit>

    suspend fun deleteClubQna(
        clubId: Int,
        qnaId: Int
    ): Result<Unit>

    suspend fun cancelClubLike(
        clubId: Int
    ): Result<Unit>
}
