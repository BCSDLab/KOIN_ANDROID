package `in`.koreatech.koin.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import `in`.koreatech.koin.data.constant.DBConstant
import `in`.koreatech.koin.data.entity.CoopShopEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CoopShopDao {
    @Query("SELECT * FROM ${DBConstant.COOP_SHOP} WHERE semester = :semester ORDER BY id")
    suspend fun getCoopShops(semester: String): List<CoopShopEntity>

    @Query("SELECT * FROM ${DBConstant.COOP_SHOP} WHERE semester = :semester ORDER BY id")
    fun observeCoopShops(semester: String): Flow<List<CoopShopEntity>>

    @Query("SELECT * FROM ${DBConstant.COOP_SHOP} WHERE id = :id AND semester = :semester LIMIT 1")
    suspend fun getCoopShopById(id: Int, semester: String): CoopShopEntity?

    @Query("SELECT * FROM ${DBConstant.COOP_SHOP} WHERE id = :id AND semester = :semester LIMIT 1")
    fun observeCoopShopById(id: Int, semester: String): Flow<CoopShopEntity?>

    @Query("SELECT * FROM ${DBConstant.COOP_SHOP} WHERE semester = (SELECT MAX(semester) FROM ${DBConstant.COOP_SHOP}) ORDER BY id")
    suspend fun getLatestCoopShops(): List<CoopShopEntity>

    @Query("SELECT * FROM ${DBConstant.COOP_SHOP} WHERE semester = (SELECT MAX(semester) FROM ${DBConstant.COOP_SHOP}) ORDER BY id")
    fun observeLatestCoopShops(): Flow<List<CoopShopEntity>>

    @Query("SELECT * FROM ${DBConstant.COOP_SHOP} WHERE coopNameId = :coopNameId ORDER BY semester DESC LIMIT 1")
    suspend fun getLatestCoopShopByCoopNameId(coopNameId: Int): CoopShopEntity?

    @Query("SELECT * FROM ${DBConstant.COOP_SHOP} WHERE coopNameId = :coopNameId ORDER BY semester DESC LIMIT 1")
    fun observeLatestCoopShopByCoopNameId(coopNameId: Int): Flow<CoopShopEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCoopShops(coopShops: List<CoopShopEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCoopShop(coopShop: CoopShopEntity)

    @Query("DELETE FROM ${DBConstant.COOP_SHOP} WHERE semester = :semester")
    suspend fun deleteCoopShops(semester: String)

    @Transaction
    suspend fun replaceCoopShops(semester: String, coopShops: List<CoopShopEntity>) {
        deleteCoopShops(semester)
        insertCoopShops(coopShops)
    }
}
