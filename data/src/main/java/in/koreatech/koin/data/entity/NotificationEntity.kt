package `in`.koreatech.koin.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import `in`.koreatech.koin.data.constant.DBConstant
import java.time.LocalDateTime

@Entity(
    tableName = DBConstant.NOTIFICATION,
    indices = [Index(value = ["originUrl"], unique = true)]
)
data class NotificationEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo val type: String,
    @ColumnInfo val datetime: LocalDateTime,
    @ColumnInfo val title: String,
    @ColumnInfo val content: String,
    @ColumnInfo val originUrl: String,
    @ColumnInfo val isRead: Boolean
)
