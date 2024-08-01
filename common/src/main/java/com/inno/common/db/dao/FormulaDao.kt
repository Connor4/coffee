package com.inno.common.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.inno.common.db.entity.Formula
import kotlinx.coroutines.flow.Flow

@Dao
interface FormulaDao {

    @Query("SELECT * FROM formula_table ORDER BY id ASC")
    fun getAllFormula(): Flow<List<Formula>>

    @Query("SELECT * FROM formula_table WHERE productId =:productId LIMIT 1")
    suspend fun getFormulaByProductId(productId: Int): Formula?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFormulaList(list: List<Formula>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFormula(formula: Formula)

    @Query("DELETE FROM formula_table WHERE id =:id")
    suspend fun deleteFormula(id: Int)

    @Update
    suspend fun updateFormula(formula: Formula)

}