package com.inno.common.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.inno.common.db.entity.DrinksHistory
import kotlinx.coroutines.flow.Flow

@Dao
interface DrinksHistoryDao {

    @Query("SELECT * FROM drinks_history_table ORDER BY id ASC")
    fun getAllDrinksHistory(): Flow<List<DrinksHistory>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(drinksHistory: DrinksHistory)

    @Query("DELETE FROM drinks_history_table")
    suspend fun deleteAll()

}