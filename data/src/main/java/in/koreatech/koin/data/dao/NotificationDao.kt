package `in`.koreatech.koin.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import `in`.koreatech.koin.data.constant.DBConstant
import `in`.koreatech.koin.data.entity.NotificationEntity
import java.time.LocalDateTime
import kotlinx.coroutines.flow.Flow

@Suppress("Detekt.TooManyFunctions")
@Dao
interface NotificationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotification(notificationEntity: NotificationEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotifications(notificationEntities: List<NotificationEntity>)

    @Query("SELECT * FROM ${DBConstant.NOTIFICATION}")
    suspend fun getNotifications(): List<NotificationEntity>

    @Query("SELECT * FROM ${DBConstant.NOTIFICATION}")
    fun getNotificationsFlow(): Flow<List<NotificationEntity>>

    @Query("SELECT * FROM ${DBConstant.NOTIFICATION} WHERE originUrl = :url")
    suspend fun getNotificationByUrl(url: String): NotificationEntity

    @Query("SELECT * FROM ${DBConstant.NOTIFICATION} WHERE id = :id")
    suspend fun getNotificationById(id: Int): NotificationEntity

    @Query("UPDATE ${DBConstant.NOTIFICATION} SET isRead = :isRead WHERE originUrl = :url")
    suspend fun updateReadByUrl(url: String, isRead: Boolean)

    @Query("UPDATE ${DBConstant.NOTIFICATION} SET isRead = :isRead WHERE id = :id")
    suspend fun updateReadById(id: Int, isRead: Boolean)

    @Transaction
    suspend fun updateReadByUrlAndReturn(url: String, isRead: Boolean): NotificationEntity {
        updateReadByUrl(url, isRead)
        return getNotificationByUrl(url)
    }

    @Transaction
    suspend fun updateReadByIdAndReturn(id: Int, isRead: Boolean): NotificationEntity {
        updateReadById(id, isRead)
        return getNotificationById(id)
    }

    @Query("DELETE FROM ${DBConstant.NOTIFICATION} WHERE datetime < :datetime")
    suspend fun deleteOldNotifications(datetime: LocalDateTime)

    @Query("DELETE FROM ${DBConstant.NOTIFICATION} WHERE id = :id")
    suspend fun deleteNotification(id: Int)

    @Query("DELETE FROM ${DBConstant.NOTIFICATION} WHERE id IN (:ids)")
    suspend fun deleteNotifications(ids: List<Int>)
}
