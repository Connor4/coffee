package com.inno.common.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.inno.common.db.entity.ProductCount

@Dao
interface ProductCountDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(productCount: ProductCount)

    @Query("DELETE FROM product_count_table")
    suspend fun deleteAll()

    @Query("SELECT COUNT(*) FROM product_count_table")
    suspend fun getProductCount(): Int

    @Query("SELECT * FROM product_count_table WHERE productId = :productId LIMIT 1")
    suspend fun getProductCountByProductId(productId: Int): ProductCount?

    @Update
    suspend fun updateProductCount(productCount: ProductCount)

}