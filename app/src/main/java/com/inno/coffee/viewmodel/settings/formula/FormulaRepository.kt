package com.inno.coffee.viewmodel.settings.formula

import com.inno.coffee.data.DrinksModel
import com.inno.coffee.viewmodel.home.HomeLocalDataSource
import com.inno.common.db.dao.FormulaDao
import com.inno.common.db.entity.Formula
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FormulaRepository @Inject constructor(
    private val localDataSource: HomeLocalDataSource,
    private val formulaDao: FormulaDao
) {
    val drinkType: List<DrinksModel> = localDataSource.drinksTypes

    fun getSynchronizedFormula(productId: Int): Flow<Formula?> {
        return formulaDao.getFormulaByProductIdFlow(productId)
    }

    fun getAllFormula(): Flow<List<Formula>> {
        return formulaDao.getAllFormula()
    }

    suspend fun getFormulaByProductId(productId: Int): Formula? {
        return formulaDao.getFormulaByProductId(productId)
    }

    suspend fun insertFormulaList(list: List<Formula>) {
        formulaDao.insertFormulaList(list)
    }

    suspend fun insertFormula(formula: Formula) {
        formulaDao.insertFormula(formula)
    }

    suspend fun deleteFormulaById(id: Int) {
        formulaDao.deleteFormula(id)
    }

    suspend fun updateFormula(formula: Formula) {
        formulaDao.updateFormula(formula)
    }

}