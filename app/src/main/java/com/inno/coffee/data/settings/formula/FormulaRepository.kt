package com.inno.coffee.data.settings.formula

import com.inno.common.db.dao.FormulaDao
import com.inno.common.db.entity.Formula
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FormulaRepository @Inject constructor(private val formulaDao: FormulaDao) {

    fun getAllFormula(): Flow<List<Formula>> {
        return formulaDao.getAllFormula()
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