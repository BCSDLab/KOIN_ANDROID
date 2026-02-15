package `in`.koreatech.koin.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import `in`.koreatech.koin.data.constant.DBConstant

@Entity(tableName = DBConstant.ABTEST)
data class ABTestEntity(
    @PrimaryKey val title: String,
    @ColumnInfo(name = "access_history_id") val accessHistoryId: String,
    @ColumnInfo(name = "variable_name") val variableName: String
)
