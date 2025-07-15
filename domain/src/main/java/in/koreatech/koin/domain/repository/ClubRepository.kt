package `in`.koreatech.koin.domain.repository

import `in`.koreatech.koin.domain.model.club.ClubCategories
import `in`.koreatech.koin.domain.model.club.ClubDetails
import `in`.koreatech.koin.domain.model.club.ClubEvent
import `in`.koreatech.koin.domain.model.club.ClubHot
import `in`.koreatech.koin.domain.model.club.ClubQnasInfo
import `in`.koreatech.koin.domain.model.club.ClubRecruitment
import `in`.koreatech.koin.domain.model.club.Clubs

interface ClubRepository {
    suspend fun getClubsCategories(): Result<ClubCategories>

    suspend fun getClubHot(): Result<ClubHot>

    suspend fun getClubs(
        categoryId: Int? = null,
        sortType: String? = null
    ): Result<Clubs>

    suspend fun getClubDetails(
        clubId: Int
    ): Result<ClubDetails>

    suspend fun createClub(
        name: String,
        imageUrl: String,
        clubManagers: List<String>,
        clubCategoryId: Int,
        location: String,
        description: String,
        instagram: String,
        googleForm: String,
        openChat: String,
        phoneNumber: String,
        role: String,
        isLikeHidden: Boolean
    ): Result<Unit>

    suspend fun modifyClub(
        clubId: Int,
        name: String,
        imageUrl: String,
        clubCategoryId: Int,
        location: String,
        description: String,
        instagram: String,
        googleForm: String,
        openChat: String,
        phoneNumber: String,
        isLikeHidden: Boolean
    ): Result<Unit>

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

    suspend fun getClubRecruitment(
        clubId: Int
    ): Result<ClubRecruitment>

    suspend fun createClubRecruitment(
        clubId: Int,
        startDate: String?,
        endDate: String?,
        isAlwaysRecruiting: Boolean,
        imageUrl: String,
        content: String
    ): Result<Unit>

    suspend fun deleteClubRecruitment(
        clubId: Int
    ): Result<Unit>

    suspend fun modifyClubRecruitment(
        clubId: Int,
        startDate: String?,
        endDate: String?,
        isAlwaysRecruiting: Boolean,
        imageUrl: String,
        content: String
    ): Result<Unit>

    suspend fun getClubEvents(
        clubId: Int,
        eventType: String
    ): Result<List<ClubEvent>>

    suspend fun createClubEvent(
        clubId: Int,
        name: String,
        imageUrls: List<String>,
        startDate: String,
        endDate: String,
        introduce: String,
        content: String?
    ): Result<Unit>

    suspend fun getClubEvent(
        clubId: Int,
        eventId: Int
    ): Result<ClubEvent>

    suspend fun modifyClubEvent(
        clubId: Int,
        eventId: Int,
        name: String,
        imageUrls: List<String>,
        startDate: String,
        endDate: String,
        introduce: String,
        content: String?
    ): Result<Unit>

    suspend fun deleteClubEvent(
        clubId: Int,
        eventId: Int
    ): Result<Unit>
}
