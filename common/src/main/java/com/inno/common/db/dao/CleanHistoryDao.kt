package com.inno.common.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.inno.common.db.entity.CleanMachineHistory
import kotlinx.coroutines.flow.Flow

@Dao
interface CleanHistoryDao {

    @Query("SELECT * FROM clean_history_table ORDER BY id ASC")
    fun getAllCleanHistory(): Flow<List<CleanMachineHistory>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCleanHistory(cleanMachineHistory: CleanMachineHistory)

    @Query("DELETE FROM clean_history_table")
    suspend fun deleteAllCleanHistory()

}