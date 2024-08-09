package com.inno.common.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.inno.common.db.entity.RinseHistory
import kotlinx.coroutines.flow.Flow

@Dao
interface RinseHistoryDao {

    @Query("SELECT * FROM rinse_history_table ORDER BY id ASC")
    fun getAllRinseHistory(): Flow<List<RinseHistory>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRinseHistory(rinseHistory: RinseHistory)

    @Query("DELETE FROM rinse_history_table")
    suspend fun deleteAllRinseHistory()

}