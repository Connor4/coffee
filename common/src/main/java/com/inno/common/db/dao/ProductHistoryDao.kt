package com.inno.common.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.inno.common.db.entity.ProductHistory
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductHistoryDao {

    @Query("SELECT * FROM product_history_table ORDER BY id ASC")
    fun getAllProductHistory(): Flow<List<ProductHistory>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProductHistory(productHistory: ProductHistory)

    @Query("DELETE FROM product_history_table")
    suspend fun deleteAllProductHistory()

    @Query("SELECT COUNT(*) FROM product_history_table")
    suspend fun getAllProductCount(): Int

    @Query("SELECT COUNT(*) FROM product_history_table WHERE brewSide == :left")
    suspend fun getBrewProductCount(left: Boolean): Int

}