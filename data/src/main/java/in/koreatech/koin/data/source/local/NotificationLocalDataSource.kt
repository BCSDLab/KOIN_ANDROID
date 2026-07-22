package `in`.koreatech.koin.data.source.local

import `in`.koreatech.koin.data.dao.NotificationDao
import `in`.koreatech.koin.data.entity.NotificationEntity
import java.time.LocalDateTime
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class NotificationLocalDataSource @Inject constructor(
    private val notificationDao: NotificationDao
) {
    suspend fun insertNotification(notificationEntity: NotificationEntity) {
        notificationDao.insertNotification(notificationEntity)
    }

    suspend fun insertNotifications(notificationEntities: List<NotificationEntity>) {
        notificationDao.insertNotifications(notificationEntities)
    }

    suspend fun getNotifications(): List<NotificationEntity> {
        return notificationDao.getNotifications()
    }

    fun getNotificationsFlow(): Flow<List<NotificationEntity>> {
        return notificationDao.getNotificationsFlow()
    }

    suspend fun updateNotificationReadByUrl(url: String, isRead: Boolean): NotificationEntity {
        return notificationDao.updateReadByUrlAndReturn(url, isRead)
    }

    suspend fun updateNotificationReadById(id: Int, isRead: Boolean): NotificationEntity {
        return notificationDao.updateReadByIdAndReturn(id, isRead)
    }

    suspend fun deleteOldNotifications(dateTime: LocalDateTime) {
        notificationDao.deleteOldNotifications(dateTime)
    }

    suspend fun deleteNotification(id: Int) {
        notificationDao.deleteNotification(id)
    }

    suspend fun deleteNotifications(ids: List<Int>) {
        notificationDao.deleteNotifications(ids)
    }
}
