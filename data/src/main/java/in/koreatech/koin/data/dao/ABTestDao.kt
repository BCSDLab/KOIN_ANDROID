package `in`.koreatech.koin.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import `in`.koreatech.koin.data.constant.DBConstant
import `in`.koreatech.koin.data.entity.ABTestEntity

@Dao
interface ABTestDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(abTestEntity: ABTestEntity)

    @Query("SELECT * FROM ${DBConstant.ABTEST} WHERE title = :title AND access_history_id = :accessHistoryId")
    suspend fun getABTest(title: String, accessHistoryId: String): ABTestEntity?
}
