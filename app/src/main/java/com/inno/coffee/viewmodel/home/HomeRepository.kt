package com.inno.coffee.viewmodel.home

import com.inno.coffee.utilities.SHOW_TYPE_LEFT
import com.inno.common.db.dao.FormulaDao
import com.inno.common.db.dao.UserDao
import com.inno.common.db.entity.Formula
import com.inno.common.utils.BcryptUtils
import com.inno.common.utils.Logger
import com.inno.common.utils.UserSessionManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class HomeRepository @Inject constructor(
    private val userDao: UserDao,
    private val formulaDao: FormulaDao,
) {
    private val tag = "HomeRepository"

    suspend fun authenticateUser(username: String, password: String): Boolean {
        val user = userDao.getUserByUserName(username)
        return user?.let {
            val checkPassword = BcryptUtils.checkPassword(password, it.passwordHash)
            if (checkPassword) {
                UserSessionManager.setUser(user)
            }
            checkPassword
        } ?: false
    }

    fun authenticateGrinder(password: String): Boolean {
        userDao.getAllUser().forEach {
            val valid = BcryptUtils.checkPassword(password, it.passwordHash)
            Logger.d(tag, "authenticateGrinder() called: password = $password, it = $it" +
                    "valid = $valid")
            if (valid) {
                return true
            }
        }
        return false
    }

    fun authenticateUserByPassword(password: String): Boolean {
        userDao.getAllUser().forEach {
            val valid = BcryptUtils.checkPassword(password, it.passwordHash)
            Logger.d(tag, "authenticateUserByPassword() called: password = $password, it = $it" +
                    "valid = $valid")
            if (valid) {
                UserSessionManager.setUser(it)
                return true
            }
        }
        return false
    }

    fun getLeftAllFormulas(): Flow<List<Formula>> {
        return formulaDao.getAllFormulaFlow().map { formulas ->
            formulas.filter { formula -> formula.showType == SHOW_TYPE_LEFT }
//                .sortedBy { formula -> formula.productId }
        }
    }

    suspend fun getAllFormulas(): List<Formula> {
        return formulaDao.getAllFormula()
    }

}