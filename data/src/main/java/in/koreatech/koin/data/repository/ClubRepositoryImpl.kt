package `in`.koreatech.koin.data.repository

import `in`.koreatech.koin.data.mapper.toClubCategories
import `in`.koreatech.koin.data.mapper.toClubDetails
import `in`.koreatech.koin.data.mapper.toClubHot
import `in`.koreatech.koin.data.mapper.toClubQnasInfo
import `in`.koreatech.koin.data.mapper.toClubs
import `in`.koreatech.koin.data.request.club.ClubCreateRequest
import `in`.koreatech.koin.data.request.club.ClubEmpowermentRequest
import `in`.koreatech.koin.data.request.club.ClubQnaRequest
import `in`.koreatech.koin.data.source.remote.ClubRemoteDataSource
import `in`.koreatech.koin.domain.model.club.ClubCategories
import `in`.koreatech.koin.domain.model.club.ClubDetails
import `in`.koreatech.koin.domain.model.club.ClubHot
import `in`.koreatech.koin.domain.model.club.ClubQnasInfo
import `in`.koreatech.koin.domain.model.club.Clubs
import `in`.koreatech.koin.domain.repository.ClubRepository
import javax.inject.Inject

class ClubRepositoryImpl @Inject constructor(
    private val clubRemoteDataSource: ClubRemoteDataSource
) : ClubRepository {
    override suspend fun getClubsCategories(): Result<ClubCategories> {
        return runCatching {
            clubRemoteDataSource.getClubsCategories().toClubCategories()
        }
    }

    override suspend fun getClubHot(): Result<ClubHot> {
        return runCatching {
            clubRemoteDataSource.getClubHot().toClubHot()
        }
    }

    override suspend fun getClubs(categoryId: Int?, sortType: String?): Result<Clubs> {
        return runCatching {
            clubRemoteDataSource.getClubs(categoryId, sortType).toClubs()
        }
    }

    override suspend fun cancelClubLike(clubId: Int): Result<Unit> {
        return clubRemoteDataSource.cancelClubLike(clubId)
    }

    override suspend fun getClubDetails(clubId: Int): Result<ClubDetails> {
        return clubRemoteDataSource.getClubDetails(clubId).map { it.toClubDetails() }
    }

    override suspend fun createClub(
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
        role: String
    ): Result<Unit> {
        return runCatching {
            clubRemoteDataSource.createClub(
                ClubCreateRequest(
                    name = name,
                    imageUrl = imageUrl,
                    clubManagers = clubManagers.map { ClubCreateRequest.ClubManagersRequest(it) },
                    clubCategoryId = clubCategoryId,
                    location = location,
                    description = description,
                    instagram = instagram,
                    googleForm = googleForm,
                    openChat = openChat,
                    phoneNumber = phoneNumber,
                    role = role
                )
            )
        }.onFailure {
            // TODO: Handle specific exceptions after get API specification
            return Result.failure(it)
        }
    }

    override suspend fun getClubQnas(clubId: Int): Result<ClubQnasInfo> {
        return clubRemoteDataSource.getClubQnas(clubId).map { it.toClubQnasInfo() }
    }

    override suspend fun setClubLike(clubId: Int): Result<Unit> {
        return clubRemoteDataSource.setClubLike(clubId)
    }

    override suspend fun deleteClubQna(clubId: Int, qnaId: Int): Result<Unit> {
        return clubRemoteDataSource.deleteClubQna(clubId, qnaId)
    }

    override suspend fun setClubEmpowerment(clubId: Int, changedManagerId: String): Result<Unit> {
        return clubRemoteDataSource.setClubEmpowerment(ClubEmpowermentRequest(clubId, changedManagerId))
    }

    override suspend fun postClubQna(clubId: Int, parentId: Int?, content: String): Result<Unit> {
        return clubRemoteDataSource.postClubQna(
            clubId,
            ClubQnaRequest(parentId, content)
        )
    }
}
