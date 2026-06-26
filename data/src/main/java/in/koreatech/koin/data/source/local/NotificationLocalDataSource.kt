package `in`.koreatech.koin.data.source.local

import `in`.koreatech.koin.data.dao.NotificationDao
import `in`.koreatech.koin.data.entity.NotificationEntity
import java.time.LocalDateTime
import javax.inject.Inject

class NotificationLocalDataSource @Inject constructor(
    val notificationDao: NotificationDao
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