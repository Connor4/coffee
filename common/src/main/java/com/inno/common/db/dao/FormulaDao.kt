package com.inno.common.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.inno.common.db.entity.Formula
import com.inno.common.db.entity.FormulaItem
import kotlinx.coroutines.flow.Flow

@Dao
interface FormulaDao {

    @Query("SELECT * FROM formula_table WHERE showType = 1 ORDER BY `index` ASC")
    fun getLeftFormulaFlow(): Flow<List<Formula>>

    @Query("SELECT * FROM formula_table WHERE showType = 2 ORDER BY `index` ASC")
    fun getRightFormulaFlow(): Flow<List<Formula>>

    @Query("SELECT * FROM formula_table ORDER BY `index` ASC")
    fun getAllFormulaFlow(): Flow<List<Formula>>

    @Query("SELECT * FROM formula_table ORDER BY `index` ASC")
    suspend fun getAllFormula(): List<Formula>

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

    @Query("SELECT * FROM formula_table WHERE productId =:productId and showType == 3 LIMIT 1")
    suspend fun getDefaultFormulaByProductId(productId: Int): Formula?

    @Query("SELECT * FROM formula_table WHERE productType =:type and showType == 3 LIMIT 1")
    suspend fun getDefaultProductTypeFormula(type: FormulaItem.FormulaProductType): Formula?

}