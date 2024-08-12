package com.inno.common.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.inno.common.db.entity.MaintenanceHistory
import kotlinx.coroutines.flow.Flow

@Dao
interface MaintenanceHistoryDao {

    @Query("SELECT * FROM maintenance_history_table ORDER BY id ASC")
    fun getAllMaintenanceHistory(): Flow<List<MaintenanceHistory>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMaintenanceHistory(history: MaintenanceHistory)

    @Query("DELETE FROM maintenance_history_table")
    suspend fun deleteAllMaintenanceHistory()

}