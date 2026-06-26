package `in`.koreatech.koin.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import `in`.koreatech.koin.data.constant.DBConstant
import `in`.koreatech.koin.data.entity.NotificationEntity
import java.time.LocalDateTime

@Dao
interface NotificationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotification(notificationEntity: NotificationEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotifications(notificationEntities: List<NotificationEntity>)

    @Query("SELECT * FROM ${DBConstant.NOTIFICATION}")
    suspend fun getNotifications(): List<NotificationEntity>

    @Query("DELETE FROM ${DBConstant.NOTIFICATION} WHERE datetime > :datetime")
    suspend fun deleteOldNotifications(datetime: LocalDateTime)

    @Query("DELETE FROM ${DBConstant.NOTIFICATION} WHERE id = :id")
    suspend fun deleteNotification(id: Int)

    @Query("DELETE FROM ${DBConstant.NOTIFICATION} WHERE id IN (:ids)")
    suspend fun deleteNotifications(ids: List<Int>)
}
