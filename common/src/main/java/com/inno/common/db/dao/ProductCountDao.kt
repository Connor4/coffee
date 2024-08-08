package com.inno.common.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.inno.common.db.entity.ProductCount

@Dao
interface ProductCountDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(productCount: ProductCount)

    @Query("DELETE FROM product_count_table")
    suspend fun deleteAll()

    @Query("SELECT COUNT(*) FROM product_count_table")
    suspend fun getProductCount(): Int
}