package `in`.koreatech.koin.domain.repository

import `in`.koreatech.koin.domain.model.recruitment.RecruitmentNotifications

interface RecruitmentRepository {
    suspend fun getNotifications(page: Int, limit: Int): Result<RecruitmentNotifications>

    suspend fun deleteAllNotifications(): Result<Unit>

    suspend fun readNotification(notificationId: Int): Result<Unit>

    suspend fun readAllNotifications(): Result<Unit>
}
