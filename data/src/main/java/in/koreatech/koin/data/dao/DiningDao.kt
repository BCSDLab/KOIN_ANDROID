package `in`.koreatech.koin.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import `in`.koreatech.koin.data.constant.DBConstant
import `in`.koreatech.koin.data.entity.DiningEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DiningDao {
    @Query("SELECT * FROM ${DBConstant.DINING} WHERE cacheDate = :cacheDate ORDER BY place")
    fun observeDining(cacheDate: String): Flow<List<DiningEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDining(dining: List<DiningEntity>)

    @Query("DELETE FROM ${DBConstant.DINING} WHERE cacheDate = :cacheDate")
    suspend fun deleteDining(cacheDate: String)

    @Transaction
    suspend fun replaceDining(cacheDate: String, dining: List<DiningEntity>) {
        deleteDining(cacheDate)
        insertDining(dining)
    }
}
