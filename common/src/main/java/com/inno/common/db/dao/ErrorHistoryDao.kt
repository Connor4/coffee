package com.inno.common.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.inno.common.db.entity.ErrorHistory
import kotlinx.coroutines.flow.Flow

@Dao
interface ErrorHistoryDao {

    @Query("SELECT * FROM error_history_table ORDER BY id ASC")
    fun getAllErrorHistory(): Flow<List<ErrorHistory>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertErrorHistory(errorHistory: ErrorHistory)

    @Query("DELETE FROM error_history_table")
    suspend fun deleteAllErrorHistory()

}