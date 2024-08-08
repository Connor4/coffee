package com.inno.common.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.inno.common.db.entity.ProductHistory
import kotlinx.coroutines.flow.Flow

@Dao
interface DrinksHistoryDao {

    @Query("SELECT * FROM drinks_history_table ORDER BY id ASC")
    fun getAllDrinksHistory(): Flow<List<ProductHistory>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(drinksHistory: ProductHistory)

    @Query("DELETE FROM drinks_history_table")
    suspend fun deleteAll()

}