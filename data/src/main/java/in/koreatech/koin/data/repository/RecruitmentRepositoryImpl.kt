package `in`.koreatech.koin.data.repository

import `in`.koreatech.koin.data.mapper.toRecruitmentNotifications
import `in`.koreatech.koin.data.source.remote.RecruitmentRemoteDataSource
import `in`.koreatech.koin.data.util.mapHttpFailure
import `in`.koreatech.koin.domain.error.recruitment.KoinRecruitmentException
import `in`.koreatech.koin.domain.model.recruitment.RecruitmentNotifications
import `in`.koreatech.koin.domain.repository.RecruitmentRepository
import `in`.koreatech.koin.domain.util.suspendRunCatching
import javax.inject.Inject

class RecruitmentRepositoryImpl @Inject constructor(
    private val recruitmentRemoteDataSource: RecruitmentRemoteDataSource
) : RecruitmentRepository {
    override suspend fun getNotifications(page: Int, limit: Int): Result<RecruitmentNotifications> {
        return suspendRunCatching {
            recruitmentRemoteDataSource.getNotifications(
                page = page,
                limit = limit
            ).toRecruitmentNotifications()
        }.mapHttpFailure {
            on(403) throws KoinRecruitmentException.ForbiddenException()
        }
    }

    override suspend fun deleteAllNotifications(): Result<Unit> {
        return suspendRunCatching {
            recruitmentRemoteDataSource.deleteAllNotifications()
        }.mapHttpFailure {
            on(403) throws KoinRecruitmentException.ForbiddenException()
        }
    }

    override suspend fun readNotification(notificationId: Int): Result<Unit> {
        return suspendRunCatching {
            recruitmentRemoteDataSource.readNotification(
                notificationId = notificationId
            )
        }.mapHttpFailure {
            on(403) throws KoinRecruitmentException.ForbiddenException()
        }
    }

    override suspend fun readAllNotifications(): Result<Unit> {
        return suspendRunCatching {
            recruitmentRemoteDataSource.readAllNotifications()
        }.mapHttpFailure {
            on(403) throws KoinRecruitmentException.ForbiddenException()
        }
    }
}
