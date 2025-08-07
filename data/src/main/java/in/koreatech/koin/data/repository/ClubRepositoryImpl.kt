package `in`.koreatech.koin.data.repository

import com.example.network.service.NetworkConnectivityService
import `in`.koreatech.koin.data.mapper.toClubCategories
import `in`.koreatech.koin.data.mapper.toClubDetails
import `in`.koreatech.koin.data.mapper.toClubEvent
import `in`.koreatech.koin.data.mapper.toClubHot
import `in`.koreatech.koin.data.mapper.toClubQnasInfo
import `in`.koreatech.koin.data.mapper.toClubRecruitment
import `in`.koreatech.koin.data.mapper.toClubSearch
import `in`.koreatech.koin.data.mapper.toClubs
import `in`.koreatech.koin.data.request.club.ClubCreateRequest
import `in`.koreatech.koin.data.request.club.ClubEmpowermentRequest
import `in`.koreatech.koin.data.request.club.ClubEventRequest
import `in`.koreatech.koin.data.request.club.ClubModifyRequest
import `in`.koreatech.koin.data.request.club.ClubQnaRequest
import `in`.koreatech.koin.data.request.club.ClubRecruitmentRequest
import `in`.koreatech.koin.data.source.remote.ClubRemoteDataSource
import `in`.koreatech.koin.data.util.getErrorResponse
import `in`.koreatech.koin.data.util.runCatchingWithNetwork
import `in`.koreatech.koin.data.util.toKoinUnknownErrorException
import `in`.koreatech.koin.domain.error.club.KoinClubException
import `in`.koreatech.koin.domain.model.club.ClubCategories
import `in`.koreatech.koin.domain.model.club.ClubDetails
import `in`.koreatech.koin.domain.model.club.ClubEvent
import `in`.koreatech.koin.domain.model.club.ClubHot
import `in`.koreatech.koin.domain.model.club.ClubQnasInfo
import `in`.koreatech.koin.domain.model.club.ClubRecruitment
import `in`.koreatech.koin.domain.model.club.ClubSearch
import `in`.koreatech.koin.domain.model.club.Clubs
import `in`.koreatech.koin.domain.repository.ClubRepository
import javax.inject.Inject
import retrofit2.HttpException

class ClubRepositoryImpl @Inject constructor(
    private val clubRemoteDataSource: ClubRemoteDataSource,
    private val networkConnectivityService: NetworkConnectivityService
) : ClubRepository {
    override suspend fun getClubsCategories(): Result<ClubCategories> {
        return runCatchingWithNetwork(networkConnectivityService) {
            clubRemoteDataSource.getClubsCategories().toClubCategories()
        }
    }

    override suspend fun getClubHot(): Result<ClubHot> {
        return runCatching {
            clubRemoteDataSource.getClubHot().toClubHot()
        }
    }

    override suspend fun getClubs(
        categoryId: Int?,
        sortType: String,
        isRecruiting: Boolean,
        query: String
    ): Result<Clubs> {
        return runCatching {
            clubRemoteDataSource.getClubs(categoryId, sortType, isRecruiting, query).toClubs()
        }.onFailure { exception ->
            return Result.failure(
                when (exception) {
                    is HttpException -> {
                        when (exception.code()) {
                            404 -> KoinClubException.ClubCategoryNotFoundException()
                            else -> exception.getErrorResponse().toKoinUnknownErrorException()
                        }
                    }

                    else -> exception
                }
            )
        }
    }

    override suspend fun cancelClubLike(clubId: Int): Result<Unit> {
        return runCatching {
            clubRemoteDataSource.cancelClubLike(clubId)
        }.onFailure { exception ->
            return Result.failure(
                when (exception) {
                    is HttpException -> {
                        when (exception.code()) {
                            401 -> KoinClubException.UnauthorizedException()
                            404 -> KoinClubException.AlreadyNotLikedException()
                            else -> exception.getErrorResponse().toKoinUnknownErrorException()
                        }
                    }

                    else -> exception
                }
            )
        }
    }

    override suspend fun getClubDetails(clubId: Int): Result<ClubDetails> {
        return runCatching {
            clubRemoteDataSource.getClubDetails(clubId).toClubDetails()
        }.onFailure { exception ->
            return Result.failure(
                when (exception) {
                    is HttpException -> {
                        when (exception.code()) {
                            404 -> KoinClubException.ClubNotFoundException()
                            else -> exception.getErrorResponse().toKoinUnknownErrorException()
                        }
                    }

                    else -> exception
                }
            )
        }
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
        role: String,
        isLikeHidden: Boolean
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
                    role = role,
                    isLikeHidden = isLikeHidden
                )
            )
        }.onFailure { exception ->
            return Result.failure(
                when (exception) {
                    is HttpException -> {
                        when (exception.code()) {
                            401 -> KoinClubException.UnauthorizedException()
                            else -> exception.getErrorResponse().toKoinUnknownErrorException()
                        }
                    }

                    else -> exception
                }
            )
        }
    }

    override suspend fun modifyClub(
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
    ): Result<Unit> {
        return runCatching {
            clubRemoteDataSource.modifyClub(
                clubId = clubId,
                request = ClubModifyRequest(
                    name = name,
                    imageUrl = imageUrl,
                    clubCategoryId = clubCategoryId,
                    location = location,
                    description = description,
                    instagram = instagram,
                    googleForm = googleForm,
                    openChat = openChat,
                    phoneNumber = phoneNumber,
                    isLikeHidden = isLikeHidden
                )
            )
        }.onFailure { exception ->
            return Result.failure(
                when (exception) {
                    is HttpException -> {
                        when (exception.code()) {
                            401 -> KoinClubException.UnauthorizedException()
                            403 -> KoinClubException.NotClubManagerException()
                            404 -> KoinClubException.ClubNotFoundException()
                            else -> exception.getErrorResponse().toKoinUnknownErrorException()
                        }
                    }

                    else -> exception
                }
            )
        }
    }

    override suspend fun getClubQnas(clubId: Int): Result<ClubQnasInfo> {
        return runCatching {
            clubRemoteDataSource.getClubQnas(clubId).toClubQnasInfo()
        }
    }

    override suspend fun setClubLike(clubId: Int): Result<Unit> {
        return runCatching {
            clubRemoteDataSource.setClubLike(clubId)
        }.onFailure { exception ->
            return Result.failure(
                when (exception) {
                    is HttpException -> {
                        when (exception.code()) {
                            401 -> KoinClubException.UnauthorizedException()
                            409 -> KoinClubException.AlreadyLikedException()
                            else -> exception.getErrorResponse().toKoinUnknownErrorException()
                        }
                    }

                    else -> exception
                }
            )
        }
    }

    override suspend fun deleteClubQna(clubId: Int, qnaId: Int): Result<Unit> {
        return runCatching {
            val response = clubRemoteDataSource.deleteClubQna(clubId, qnaId)
            if (response.isSuccessful) {
                Unit
            } else {
                throw HttpException(response)
            }
        }.onFailure { exception ->
            return Result.failure(
                when (exception) {
                    is HttpException -> {
                        when (exception.code()) {
                            403 -> KoinClubException.DeletePermissionDeniedException()
                            404 -> KoinClubException.QnaNotFoundException()
                            else -> exception.getErrorResponse().toKoinUnknownErrorException()
                        }
                    }

                    else -> exception
                }
            )
        }
    }

    override suspend fun setClubEmpowerment(clubId: Int, changedManagerId: String): Result<Unit> {
        return runCatching {
            clubRemoteDataSource.setClubEmpowerment(ClubEmpowermentRequest(clubId, changedManagerId))
        }.onFailure { exception ->
            return Result.failure(
                when (exception) {
                    is HttpException -> {
                        when (exception.code()) {
                            400 -> KoinClubException.AlreadyManagerException()
                            401 -> KoinClubException.UnauthorizedException()
                            403 -> KoinClubException.NotClubManagerException()
                            404 -> KoinClubException.LoginIdNotFoundException()
                            else -> exception.getErrorResponse().toKoinUnknownErrorException()
                        }
                    }

                    else -> exception
                }
            )
        }
    }

    override suspend fun postClubQna(clubId: Int, parentId: Int?, content: String): Result<Unit> {
        return runCatching {
            clubRemoteDataSource.postClubQna(
                clubId,
                ClubQnaRequest(parentId, content)
            )
        }.onFailure { exception ->
            return Result.failure(
                when (exception) {
                    is HttpException -> {
                        when (exception.code()) {
                            401 -> KoinClubException.UnauthorizedException()
                            403 -> KoinClubException.NotClubManagerException()
                            404 -> KoinClubException.ClubNotFoundException()
                            else -> exception.getErrorResponse().toKoinUnknownErrorException()
                        }
                    }

                    else -> exception
                }
            )
        }
    }

    override suspend fun getClubRecruitment(
        clubId: Int
    ): Result<ClubRecruitment> {
        return runCatching {
            clubRemoteDataSource.getClubRecruitment(clubId).toClubRecruitment()
        }.onFailure { exception ->
            return Result.failure(
                when (exception) {
                    is HttpException -> {
                        when (exception.code()) {
                            400 -> KoinClubException.WrongInputDataException()
                            404 -> KoinClubException.ClubRecruitNotFoundException()
                            else -> exception.getErrorResponse().toKoinUnknownErrorException()
                        }
                    }
                    else -> exception
                }
            )
        }
    }

    override suspend fun createClubRecruitment(
        clubId: Int,
        startDate: String?,
        endDate: String?,
        isAlwaysRecruiting: Boolean,
        imageUrl: String,
        content: String
    ): Result<Unit> {
        return runCatching {
            clubRemoteDataSource.createClubRecruitment(
                clubId,
                ClubRecruitmentRequest(
                    startDate,
                    endDate,
                    isAlwaysRecruiting,
                    imageUrl,
                    content
                )
            )
        }.onFailure { exception ->
            return Result.failure(
                when (exception) {
                    is HttpException -> {
                        when (exception.code()) {
                            400 -> KoinClubException.WrongInputDataException()
                            404 -> KoinClubException.ClubRecruitNotFoundException()
                            409 -> KoinClubException.AlreadyRecruitingException()
                            else -> exception.getErrorResponse().toKoinUnknownErrorException()
                        }
                    }
                    else -> exception
                }
            )
        }
    }

    override suspend fun deleteClubRecruitment(clubId: Int): Result<Unit> {
        return runCatching {
            val response = clubRemoteDataSource.deleteClubRecruitment(clubId)
            if (response.isSuccessful) {
                Unit
            } else {
                throw HttpException(response)
            }
        }.onFailure { exception ->
            return Result.failure(
                when (exception) {
                    is HttpException -> {
                        when (exception.code()) {
                            400 -> KoinClubException.WrongInputDataException()
                            404 -> KoinClubException.ClubRecruitNotFoundException()
                            else -> exception.getErrorResponse().toKoinUnknownErrorException()
                        }
                    }
                    else -> exception
                }
            )
        }
    }

    override suspend fun modifyClubRecruitment(
        clubId: Int,
        startDate: String?,
        endDate: String?,
        isAlwaysRecruiting: Boolean,
        imageUrl: String,
        content: String
    ): Result<Unit> {
        return runCatching {
            val response = clubRemoteDataSource.modifyClubRecruitment(
                clubId,
                ClubRecruitmentRequest(
                    startDate,
                    endDate,
                    isAlwaysRecruiting,
                    imageUrl,
                    content
                )
            )
            if (response.isSuccessful) {
                Unit
            } else {
                throw HttpException(response)
            }
        }.onFailure { exception ->
            return Result.failure(
                when (exception) {
                    is HttpException -> {
                        when (exception.code()) {
                            400 -> KoinClubException.WrongInputDataException()
                            404 -> KoinClubException.ClubRecruitNotFoundException()
                            else -> exception.getErrorResponse().toKoinUnknownErrorException()
                        }
                    }
                    else -> exception
                }
            )
        }
    }

    override suspend fun getClubEvents(
        clubId: Int,
        eventType: String
    ): Result<List<ClubEvent>> {
        return runCatching {
            clubRemoteDataSource.getClubEvents(clubId, eventType).map { it.toClubEvent() }
        }.onFailure { exception ->
            return Result.failure(
                when (exception) {
                    is HttpException -> {
                        when (exception.code()) {
                            400 -> KoinClubException.WrongInputDataException()
                            404 -> KoinClubException.ClubEventNotFoundException()
                            else -> exception.getErrorResponse().toKoinUnknownErrorException()
                        }
                    }
                    else -> exception
                }
            )
        }
    }

    override suspend fun createClubEvent(
        clubId: Int,
        name: String,
        imageUrls: List<String>,
        startDate: String,
        endDate: String,
        introduce: String,
        content: String?
    ): Result<Unit> {
        return runCatching {
            val response = clubRemoteDataSource.createClubEvent(
                clubId,
                ClubEventRequest(
                    name,
                    imageUrls,
                    startDate,
                    endDate,
                    introduce,
                    content
                )
            )
            if (response.isSuccessful) {
                Unit
            } else {
                throw HttpException(response)
            }
        }.onFailure { exception ->
            return Result.failure(
                when (exception) {
                    is HttpException -> {
                        when (exception.code()) {
                            400 -> KoinClubException.WrongInputDataException()
                            404 -> KoinClubException.ClubNotFoundException()
                            else -> exception.getErrorResponse().toKoinUnknownErrorException()
                        }
                    }
                    else -> exception
                }
            )
        }
    }

    override suspend fun getClubEvent(
        clubId: Int,
        eventId: Int
    ): Result<ClubEvent> {
        return runCatching {
            clubRemoteDataSource.getClubEvent(clubId, eventId).toClubEvent()
        }.onFailure { exception ->
            return Result.failure(
                when (exception) {
                    is HttpException -> {
                        when (exception.code()) {
                            400 -> KoinClubException.WrongInputDataException()
                            404 -> KoinClubException.ClubEventNotFoundException()
                            else -> exception.getErrorResponse().toKoinUnknownErrorException()
                        }
                    }
                    else -> exception
                }
            )
        }
    }

    override suspend fun searchClubs(query: String): Result<ClubSearch> {
        return runCatching {
            clubRemoteDataSource.searchClubs(query).toClubSearch()
        }.onFailure { exception ->
            return Result.failure(
                when (exception) {
                    is HttpException -> {
                        when (exception.code()) {
                            404 -> KoinClubException.ClubNotFoundException()
                            else -> exception.getErrorResponse().toKoinUnknownErrorException()
                        }
                    }

                    else -> exception
                }
            )
        }
    }

    override suspend fun modifyClubEvent(
        clubId: Int,
        eventId: Int,
        name: String,
        imageUrls: List<String>,
        startDate: String,
        endDate: String,
        introduce: String,
        content: String?
    ): Result<Unit> {
        return runCatching {
            val response = clubRemoteDataSource.modifyClubEvent(
                clubId,
                eventId,
                ClubEventRequest(
                    name,
                    imageUrls,
                    startDate,
                    endDate,
                    introduce,
                    content
                )
            )
            if (response.isSuccessful) {
                Unit
            } else {
                throw HttpException(response)
            }
        }.onFailure { exception ->
            return Result.failure(
                when (exception) {
                    is HttpException -> {
                        when (exception.code()) {
                            400 -> KoinClubException.WrongInputDataException()
                            404 -> KoinClubException.ClubEventNotFoundException()
                            else -> exception.getErrorResponse().toKoinUnknownErrorException()
                        }
                    }
                    else -> exception
                }
            )
        }
    }

    override suspend fun deleteClubEvent(
        clubId: Int,
        eventId: Int
    ): Result<Unit> {
        return runCatching {
            val response = clubRemoteDataSource.deleteClubEvent(clubId, eventId)
            if (response.isSuccessful) {
                Unit
            } else {
                throw HttpException(response)
            }
        }.onFailure { exception ->
            return Result.failure(
                when (exception) {
                    is HttpException -> {
                        when (exception.code()) {
                            400 -> KoinClubException.WrongInputDataException()
                            404 -> KoinClubException.ClubEventNotFoundException()
                            else -> exception.getErrorResponse().toKoinUnknownErrorException()
                        }
                    }
                    else -> exception
                }
            )
        }
    }

    override suspend fun subscribeClubRecruitment(clubId: Int): Result<Unit> {
        return runCatching {
            val response = clubRemoteDataSource.subscribeClubRecruitment(clubId)
            if (response.isSuccessful) {
                Unit
            } else {
                throw HttpException(response)
            }
        }.onFailure { exception ->
            return Result.failure(
                when (exception) {
                    is HttpException -> {
                        when (exception.code()) {
                            400 -> KoinClubException.WrongInputDataException()
                            403 -> KoinClubException.NotAllowedUserException()
                            404 -> KoinClubException.ClubNotFoundException()
                            else -> exception.getErrorResponse().toKoinUnknownErrorException()
                        }
                    }
                    else -> exception
                }
            )
        }
    }

    override suspend fun unsubscribeClubRecruitment(clubId: Int): Result<Unit> {
        return runCatching {
            val response = clubRemoteDataSource.unsubscribeClubRecruitment(clubId)
            if (response.isSuccessful) {
                Unit
            } else {
                throw HttpException(response)
            }
        }.onFailure { exception ->
            return Result.failure(
                when (exception) {
                    is HttpException -> {
                        when (exception.code()) {
                            400 -> KoinClubException.WrongInputDataException()
                            403 -> KoinClubException.NotAllowedUserException()
                            404 -> KoinClubException.ClubNotFoundException()
                            else -> exception.getErrorResponse().toKoinUnknownErrorException()
                        }
                    }
                    else -> exception
                }
            )
        }
    }

    override suspend fun subscribeClubEvent(
        clubId: Int,
        eventId: Int
    ): Result<Unit> {
        return runCatching {
            val response = clubRemoteDataSource.subscribeClubEvent(clubId, eventId)
            if (response.isSuccessful) {
                Unit
            } else {
                throw HttpException(response)
            }
        }.onFailure { exception ->
            return Result.failure(
                when (exception) {
                    is HttpException -> {
                        when (exception.code()) {
                            400 -> KoinClubException.WrongInputDataException()
                            403 -> KoinClubException.NotAllowedUserException()
                            404 -> KoinClubException.ClubEventNotFoundException()
                            else -> exception.getErrorResponse().toKoinUnknownErrorException()
                        }
                    }
                    else -> exception
                }
            )
        }
    }

    override suspend fun unsubscribeClubEvent(
        clubId: Int,
        eventId: Int
    ): Result<Unit> {
        return runCatching {
            val response = clubRemoteDataSource.unsubscribeClubEvent(clubId, eventId)
            if (response.isSuccessful) {
                Unit
            } else {
                throw HttpException(response)
            }
        }.onFailure { exception ->
            return Result.failure(
                when (exception) {
                    is HttpException -> {
                        when (exception.code()) {
                            400 -> KoinClubException.WrongInputDataException()
                            403 -> KoinClubException.NotAllowedUserException()
                            404 -> KoinClubException.ClubEventNotFoundException()
                            else -> exception.getErrorResponse().toKoinUnknownErrorException()
                        }
                    }
                    else -> exception
                }
            )
        }
    }
}
