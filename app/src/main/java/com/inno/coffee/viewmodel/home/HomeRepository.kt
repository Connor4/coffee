package com.inno.coffee.viewmodel.home

import com.inno.coffee.data.DrinksModel
import com.inno.common.db.dao.UserDao
import com.inno.common.utils.BcryptUtils
import com.inno.common.utils.UserSessionManager
import javax.inject.Inject

class HomeRepository @Inject constructor(
    private val homeLocalDataSource: HomeLocalDataSource,
    private val userDao: UserDao
) {
    val drinksType: List<DrinksModel> = homeLocalDataSource.drinksTypes
    val specialItem = homeLocalDataSource.specialItem

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

}