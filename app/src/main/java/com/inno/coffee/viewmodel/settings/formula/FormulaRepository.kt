package com.inno.coffee.viewmodel.settings.formula

import com.inno.common.db.dao.FormulaDao
import com.inno.common.db.entity.Formula
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FormulaRepository @Inject constructor(
    private val formulaDao: FormulaDao
) {
    private val _updateFormula = MutableStateFlow("")
    val updateFormula = _updateFormula

    fun updateFormulaFlag() {
        _updateFormula.value = System.currentTimeMillis().toString()
    }

    fun getAllFormulaFlow(): Flow<List<Formula>> {
        return formulaDao.getAllFormulaFlow()
    }

    suspend fun getAllFormula(): List<Formula> {
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