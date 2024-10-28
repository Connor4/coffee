package com.inno.common.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.inno.common.db.entity.ProductCount
import com.inno.common.db.entity.ProductTypeCount

@Dao
interface ProductCountDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(productCount: ProductCount)

    @Query("DELETE FROM product_count_table")
    suspend fun deleteAll()

//    @Query("SELECT COUNT(*) FROM product_count_table")
//    suspend fun getProductCount(): Int

//    @Query("SELECT * FROM product_count_table WHERE type = :type LIMIT 1")
//    suspend fun getProductCountByType(type: ProductType): ProductCount?

    @Update
    suspend fun updateProductCount(productCount: ProductCount)

//    @Query("SELECT * FROM product_count_table")
//    suspend fun getAllProductCounts(): List<ProductCount>

    @Query("SELECT COUNT(*) FROM product_count_table WHERE productId = :productId")
    suspend fun getProductCountByProductId(productId: Int): Int

    @Query("SELECT type, SUM(count) as totalCount FROM product_count_table GROUP BY type")
    suspend fun getTypeCounts(): List<ProductTypeCount>

//    @Query("SELECT * FROM product_count_table WHERE type = :type")
//    suspend fun getProductCountsByType(type: ProductType): List<ProductCount>

}