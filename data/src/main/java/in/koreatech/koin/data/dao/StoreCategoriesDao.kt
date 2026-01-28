package `in`.koreatech.koin.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import `in`.koreatech.koin.data.constant.DBConstant
import `in`.koreatech.koin.data.entity.StoreCategoriesEntity

@Dao
interface StoreCategoriesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(storeCategories: List<StoreCategoriesEntity>)

    @Query("SELECT * FROM ${DBConstant.STORE_CATEGORIES}")
    fun getAll(): List<StoreCategoriesEntity>
}
